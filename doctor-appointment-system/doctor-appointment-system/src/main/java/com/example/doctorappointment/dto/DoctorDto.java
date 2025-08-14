package com.example.doctorappointment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;
}