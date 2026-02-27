package com.jamillyferreira.veterinaryclinic.dto.appointment;

import com.jamillyferreira.veterinaryclinic.dto.pet.PetSummaryDTO;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;

import java.time.OffsetDateTime;

public record AppointmentResponseDTO(
        PetSummaryDTO pet,

        OffsetDateTime scheduledAt,
        String reason,
        AppointmentStatus status
) {
}
