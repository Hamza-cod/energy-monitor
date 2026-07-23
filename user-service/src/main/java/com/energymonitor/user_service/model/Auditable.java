package com.energymonitor.user_service.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.UUID;

@MappedSuperclass
@Data
public class Auditable {
    @Id
    @GeneratedValue
    private UUID id;
}
