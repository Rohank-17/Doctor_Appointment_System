package com.example.doctorappointment.dto;

import com.example.doctorappointment.entity.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @Future(message = "Appointment date must be in the future")
    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDateTime;

    private String notes;

    private AppointmentStatus status;
}