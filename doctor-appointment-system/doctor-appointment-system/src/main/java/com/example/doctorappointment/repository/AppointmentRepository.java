package com.example.doctorappointment.repository;

import com.example.doctorappointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(
            Long doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(
            Long patientId, LocalDateTime start, LocalDateTime end);

    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);

    boolean existsByPatientIdAndAppointmentDateTime(Long patientId, LocalDateTime appointmentDateTime);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}