package com.example.doctorappointment.dto;

import com.example.doctorappointment.entity.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AppointmentResponseDto {
    private Long id;
    private DoctorDto doctor;
    private PatientDto patient;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
}