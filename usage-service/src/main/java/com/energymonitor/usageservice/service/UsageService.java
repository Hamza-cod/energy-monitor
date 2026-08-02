package com.energymonitor.usageservice.service;

import com.energymonitor.common.dto.UsageDto;
import com.energymonitor.usageservice.config.InfluxDbProperties;
import com.energymonitor.common.dto.DeviceDto;
import com.energymonitor.common.dto.UserDto;
import com.energymonitor.usageservice.http.DeviceClient;
import com.energymonitor.usageservice.http.UserClient;
import com.energymonitor.common.events.AlertingEvent;
import com.energymonitor.common.events.EnergyUsageEvent;
import com.energymonitor.usageservice.model.DeviceEnergy;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.energymonitor.common.events.StaticEventNames.*;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class UsageService {
    private final InfluxDbProperties influxDbProperties;
    private final InfluxDBClient influxDBClient;
    private final DeviceClient deviceClient;
    private final UserClient userClient;
    private final KafkaTemplate<String, AlertingEvent> kafkaTemplate;



    @KafkaListener(topics = ENERGY_USAGE_TOPIC)
    public void consumeUsage(EnergyUsageEvent event){

        log.info("Consuming energy usage event: {}", event);
        Point point = Point.measurement("energy_usage")
                .time(event.timestamp(), WritePrecision.MS)
                .addTag("device_id", event.deviceId())
                .addField("usage", event.energyConsumed());
        influxDBClient.getWriteApiBlocking().writePoint(point);
    }
    @KafkaListener(topics = USAGE_TOPIC_DLT)
    public void consumeUsageDlt(
            ConsumerRecord<String, EnergyUsageEvent> record) {

        log.error("Message reached DLT");

        log.error("Topic: {}", record.topic());
        log.error("Partition: {}", record.partition());
        log.error("Offset: {}", record.offset());

        EnergyUsageEvent event = record.value();

        // inspect headers
        record.headers().forEach(header ->
                log.error("{} = {}", header.key(), new String(header.value())));

        // send alert
    }



    @Scheduled(cron = "*/10 * * * * *")
    public void aggregateEnergyUsage(){
        final Instant now = Instant.now();
        final Instant oneHourAgo = now.minus(2, ChronoUnit.HOURS);

        String fluxQuery = String.format("""
        from(bucket: "%s")
          |> range(start: time(v: "%s"), stop: time(v: "%s"))
          |> filter(fn: (r) => r["_measurement"] == "energy_usage")
          |> filter(fn: (r) => r["_field"] == "usage")
          |> group(columns: ["device_id"])
          |> sum(column: "_value")
        """, influxDbProperties.bucket(), oneHourAgo.toString(), now);
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, influxDbProperties.org());

        List<DeviceEnergy> deviceEnergies = new ArrayList<>();

        Map<String, Double> userThresholdMap = new HashMap<>();
        Map<String, String> userEmailMap = new HashMap<>();
        // load data
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String deviceIdStr = (String) record.getValueByKey("device_id");
                double energyConsumed = record.getValueByKey("_value") instanceof Number ?
                        ((Number) Objects.requireNonNull(record.getValueByKey("_value"))).doubleValue() : 0.0;

                deviceEnergies.add(
                        DeviceEnergy.builder()
                                .deviceId(deviceIdStr)
                                .energyConsumed(energyConsumed)
                                .build()
                );
            }
        }

        // get device user
        for (DeviceEnergy deviceEnergy : deviceEnergies) {
            try {
                final DeviceDto deviceResponse = deviceClient.getDeviceById(deviceEnergy.getDeviceId());

                if (deviceResponse == null || deviceResponse.getId() == null) {
                    log.warn("Device not found for ID: {}", deviceEnergy.getDeviceId());
                    continue;
                }
                deviceEnergy.setUserId(deviceResponse.getUserId());
            } catch (Exception e) {
                log.warn("Failed to fetch device for ID: {}", deviceEnergy.getDeviceId(), e);
            }
        }

        // Devices whose lookup failed have no userId; grouping on a null key would throw.
        Map<String , List<DeviceEnergy>> deviceUserMap = deviceEnergies.stream()
                .filter(deviceEnergy -> deviceEnergy.getUserId() != null)
                .collect(Collectors.groupingBy(DeviceEnergy::getUserId));


        List<String> usersIds = new ArrayList<>(deviceUserMap.keySet());
        usersIds.forEach(userId -> {
            UserDto user = userClient.getUserById(userId);
            userThresholdMap.put(userId, user.getEnergyAlertingThreshold());
            userEmailMap.put(userId, user.getEmail());
        });

        // check threshold against the aggregated energy usage
        final List<String> alertedUsers = new ArrayList<>(userThresholdMap.keySet());
        alertedUsers.forEach(userId -> {
            Double threshold = userThresholdMap.get(userId);
            String email = userEmailMap.get(userId);
            // Perform alerting logic here
            double energyConsumed = deviceEnergies.stream()
                    .filter((deviceEnergy ->
                            deviceEnergy.getUserId().equals(userId)))
                    .mapToDouble(DeviceEnergy::getEnergyConsumed).sum();
            if (energyConsumed > threshold) {
                // Perform alerting logic here
                log.info("User {} has exceeded their energy alerting threshold.", userId);
                // send message to alerting kafka topic
                AlertingEvent event = AlertingEvent.builder()
                        .userId(userId)
                        .threshold(threshold)
                        .energyConsumed(energyConsumed)
                        .email(email)
                        .message("Energy consumption has exceeded the alerting threshold.")
                        .build();
                kafkaTemplate.send(ALERT_USAGE_TOPIC,event);
            }
        });
    }




    public UsageDto getXDaysUsageForUser(String userId, int days) {
        log.info("Getting usage for userId {} over past {} days", userId, days);

        final List<DeviceDto> devices;
        try {
            devices = deviceClient.getAllDevicesForUser(userId);
        } catch (Exception e) {
            log.error("Failed to fetch devices for user {}: {}", userId, e.getMessage());
            return UsageDto.builder().userId(userId).devices(List.of()).build();
        }

        if (devices == null || devices.isEmpty()) {
            log.info("No devices found for user {}", userId);
            return UsageDto.builder().userId(userId).devices(List.of()).build();
        }

        final List<String> deviceIds = devices.stream()
                .map(DeviceDto::getId)
                .filter(Objects::nonNull)
                .toList();

        if (deviceIds.isEmpty()) {
            devices.forEach(device -> device.setEnergyConsumed(0.0));
            return UsageDto.builder().userId(userId).devices(devices).build();
        }

        final Instant now = Instant.now();
        final Instant start = now.minus(days, ChronoUnit.DAYS);

        // build device filter: r["device_id"] == "1" or r["device_id"] == "2"
        final String deviceFilter = deviceIds.stream()
                .map(deviceId -> String.format("r[\"device_id\"] == \"%s\"", deviceId))
                .collect(Collectors.joining(" or "));

        // tag/field names must match what consumeUsage() writes
        String fluxQuery = String.format("""
        from(bucket: "%s")
          |> range(start: time(v: "%s"), stop: time(v: "%s"))
          |> filter(fn: (r) => r["_measurement"] == "energy_usage")
          |> filter(fn: (r) => r["_field"] == "usage")
          |> filter(fn: (r) => %s)
          |> group(columns: ["device_id"])
          |> sum(column: "_value")
        """, influxDbProperties.bucket(), start, now, deviceFilter);

        final Map<String, Double> aggregatedMap = new HashMap<>();

        try {
            QueryApi queryApi = influxDBClient.getQueryApi();
            List<FluxTable> tables = queryApi.query(fluxQuery, influxDbProperties.org());

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    Object deviceIdObj = record.getValueByKey("device_id");
                    if (deviceIdObj == null) continue;

                    double energyConsumed = record.getValueByKey("_value") instanceof Number value
                            ? value.doubleValue()
                            : 0.0;

                    aggregatedMap.merge(deviceIdObj.toString(), energyConsumed, Double::sum);
                }
            }
        } catch (Exception e) {
            log.error("Failed to query InfluxDB for user {} usage over {} days: {}", userId, days, e.getMessage());
            devices.forEach(device -> device.setEnergyConsumed(0.0));
            return UsageDto.builder().userId(userId).devices(devices).build();
        }

        // populate aggregated energy consumed per device
        for (DeviceDto device : devices) {
            device.setEnergyConsumed(
                    device.getId() == null ? 0.0 : aggregatedMap.getOrDefault(device.getId(), 0.0));
        }

        log.info("Aggregated energy consumption for userId {}: {}", userId, aggregatedMap);

        return UsageDto.builder()
                .userId(userId)
                .devices(devices)
                .build();
    }
}
