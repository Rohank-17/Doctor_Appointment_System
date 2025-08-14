package com.example.doctorappointment.controller;

import com.example.doctorappointment.dto.DoctorDto;
import com.example.doctorappointment.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorDto> addDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        DoctorDto savedDoctor = doctorService.addDoctor(doctorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(
            @PathVariable Long id, @Valid @RequestBody DoctorDto doctorDto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}