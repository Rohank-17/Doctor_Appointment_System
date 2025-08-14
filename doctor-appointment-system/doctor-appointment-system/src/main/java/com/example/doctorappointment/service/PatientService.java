package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.PatientDto;
import com.example.doctorappointment.entity.Patient;
import com.example.doctorappointment.exception.ResourceAlreadyExistsException;
import com.example.doctorappointment.exception.ResourceNotFoundException;
import com.example.doctorappointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public PatientDto addPatient(PatientDto patientDto) {
        if (patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Patient with email " + patientDto.getEmail() + " already exists");
        }

        Patient patient = modelMapper.map(patientDto, Patient.class);
        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient, PatientDto.class);
    }

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .collect(Collectors.toList());
    }

    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return modelMapper.map(patient, PatientDto.class);
    }

    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (!existingPatient.getEmail().equals(patientDto.getEmail())) {
            if (patientRepository.existsByEmail(patientDto.getEmail())) {
                throw new ResourceAlreadyExistsException("Email " + patientDto.getEmail() + " is already taken");
            }
        }

        modelMapper.map(patientDto, existingPatient);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return modelMapper.map(updatedPatient, PatientDto.class);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}