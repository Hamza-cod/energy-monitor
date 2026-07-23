package com.energymonitor.user_service.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class BaseUserDto {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(max = 100, message = "Surname cannot exceed 100 characters")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    private boolean alerting;

    @PositiveOrZero(message = "Energy alerting threshold must be greater than or equal to 0")
    private double energyAlertingThreshold;
}
