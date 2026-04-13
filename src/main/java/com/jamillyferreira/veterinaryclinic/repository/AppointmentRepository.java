package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    boolean existsByPetId(Long petId);

    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a
            WHERE a.veterinary.id = :veterinaryId
            AND a.status != 'CANCELADA'
            AND a.scheduledAt < :end
            AND a.scheduledAt >= :start
            """)
    boolean existsConflictingAppointment(
            @Param("veterinaryId") Long veterinaryId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
