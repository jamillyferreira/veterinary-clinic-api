package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPetId(Long petId);
}
