package com.jamillyferreira.veterinaryclinic.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record AppointmentCreateDTO(
        @NotNull(message = "Pet é obrigatório")
        Long petId,

        @NotNull(message = "Veterinário é obrigatório")
        Long veterinaryId,

        @NotBlank(message = "O motivo é obrigatório")
        @Size(max = 200, message = "Motivo deve ter no máximo 200 caracteres")
        String reason,

        @NotNull(message = "Data e Hora são obrigatórias")
        @Future(message = "A consulta deve ser agendada para um data futura")
        LocalDateTime scheduledAt
) {
}
