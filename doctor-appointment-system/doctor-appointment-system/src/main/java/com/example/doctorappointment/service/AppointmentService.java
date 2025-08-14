package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.AppointmentDto;
import com.example.doctorappointment.dto.AppointmentResponseDto;
import com.example.doctorappointment.dto.DoctorDto;
import com.example.doctorappointment.dto.PatientDto;
import com.example.doctorappointment.entity.*;
import com.example.doctorappointment.exception.ConflictException;
import com.example.doctorappointment.exception.ResourceNotFoundException;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.repository.DoctorRepository;
import com.example.doctorappointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public AppointmentResponseDto bookAppointment(AppointmentDto appointmentDto) {
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + appointmentDto.getDoctorId()));

        Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + appointmentDto.getPatientId()));

        if (appointmentRepository.existsByDoctorIdAndAppointmentDateTime(
                appointmentDto.getDoctorId(), appointmentDto.getAppointmentDateTime())) {
            throw new ConflictException("Doctor already has an appointment at this time");
        }

        if (appointmentRepository.existsByPatientIdAndAppointmentDateTime(
                appointmentDto.getPatientId(), appointmentDto.getAppointmentDateTime())) {
            throw new ConflictException("Patient already has an appointment at this time");
        }

        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToResponseDto(savedAppointment);
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return convertToResponseDto(appointment);
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ConflictException("Appointment is already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private AppointmentResponseDto convertToResponseDto(Appointment appointment) {
        AppointmentResponseDto responseDto = modelMapper.map(appointment, AppointmentResponseDto.class);
        responseDto.setDoctor(modelMapper.map(appointment.getDoctor(), DoctorDto.class));
        responseDto.setPatient(modelMapper.map(appointment.getPatient(), PatientDto.class));
        return responseDto;
    }
}