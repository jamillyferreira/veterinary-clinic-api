package com.jamillyferreira.veterinaryclinic.dto.veterinary;

import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VeterinaryCreateDTO(
        @NotBlank(message = "Nome do veterinário é obrigatório")
        String name,

        @NotBlank(message = "O CRMV é obrigatório")
        String crmv,

        @NotBlank(message = "Contato de telefone é obrigatório")
        @Pattern(regexp = "^\\d{2}-\\d{8,9}$", message = "Telefone inválido. Use o formato: 11-999999999")
        String contact,

        @NotNull(message = "Especialidade é obrigatória")
        Specialty specialty
) {
}
