package com.energymonitor.deviceservice.repository;

import com.energymonitor.deviceservice.entity.Device;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeviceRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    static {
        postgres.start();
    }

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void shouldReturnListOfDevicesByUserId() {
        Device d1 = new Device();
        d1.setUserId("user1");

        Device d2 = new Device();
        d2.setUserId("user1");

        Device d3 = new Device();
        d3.setUserId("user2");

         deviceRepository.saveAll(List.of(d1, d2, d3));

        List<Device> result = deviceRepository.findAllByUserId("user1");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getUserId().equals("user1")));
    }
}