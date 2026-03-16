package com.jamillyferreira.veterinaryclinic.dto.appointment;

import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;

import java.time.OffsetDateTime;

public record AppointmentSummaryDTO(
        Long id,
        AppointmentStatus status,
        OffsetDateTime cancelledAt
) {
}
