package com.jamillyferreira.veterinaryclinic.dto.appointment;

import jakarta.validation.constraints.NotBlank;

public record ClinicalDataInputDTO(
        @NotBlank(message = "O diagnóstico é obrigatório")
        String diagnosis,

        String observations
) {
}
