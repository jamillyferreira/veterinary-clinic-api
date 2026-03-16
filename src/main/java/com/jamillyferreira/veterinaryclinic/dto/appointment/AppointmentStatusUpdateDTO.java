package com.jamillyferreira.veterinaryclinic.dto.appointment;

import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentStatusUpdateDTO(
        @NotNull(message = "O novo status é obrigatório")
        AppointmentStatus status
) {
}
