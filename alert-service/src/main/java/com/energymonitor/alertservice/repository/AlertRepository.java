package com.energymonitor.alertservice.repository;

import com.energymonitor.alertservice.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository  extends JpaRepository<Alert, Long> {
}
