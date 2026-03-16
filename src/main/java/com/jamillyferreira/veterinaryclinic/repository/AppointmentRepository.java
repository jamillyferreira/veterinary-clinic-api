package com.jamillyferreira.veterinaryclinic.repository;

import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import com.jamillyferreira.veterinaryclinic.entity.Pet;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
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
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end
    );

    List<Appointment> findByPetId(Long petId);

    List<Appointment> findByVeterinaryId(Long veterinaryId);

    List<Appointment> findByStatus(AppointmentStatus status);
}
