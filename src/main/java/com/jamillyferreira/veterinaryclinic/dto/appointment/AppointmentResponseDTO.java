package com.jamillyferreira.veterinaryclinic.dto.appointment;

import com.jamillyferreira.veterinaryclinic.dto.pet.PetSummaryDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinarySummaryDTO;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record AppointmentResponseDTO(
        Long id,
        PetSummaryDTO pet,
        VeterinarySummaryDTO veterinary,
        String reason,
        AppointmentStatus status,
        LocalDateTime scheduledAt
) {
}
