package com.jamillyferreira.veterinaryclinic.dto.veterinary;

import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record VeterinaryUpdateDTO(
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
        String name,

        @Pattern(regexp = "^\\d{2}-\\d{8,9}$", message = "Telefone inválido. Use o formato: 11-999999999")
        String contact,

        String crmv,
        Specialty specialty
) {
}
