package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.DoctorDto;
import com.example.doctorappointment.entity.Doctor;
import com.example.doctorappointment.exception.ResourceAlreadyExistsException;
import com.example.doctorappointment.exception.ResourceNotFoundException;
import com.example.doctorappointment.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public DoctorDto addDoctor(DoctorDto doctorDto) {
        if (doctorRepository.existsByEmail(doctorDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Doctor with email " + doctorDto.getEmail() + " already exists");
        }

        Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorDto.class);
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDto.class))
                .collect(Collectors.toList());
    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return modelMapper.map(doctor, DoctorDto.class);
    }

    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        if (!existingDoctor.getEmail().equals(doctorDto.getEmail())) {
            if (doctorRepository.existsByEmail(doctorDto.getEmail())) {
                throw new ResourceAlreadyExistsException("Email " + doctorDto.getEmail() + " is already taken");
            }
        }

        modelMapper.map(doctorDto, existingDoctor);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return modelMapper.map(updatedDoctor, DoctorDto.class);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
}