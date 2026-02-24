package com.jamillyferreira.veterinaryclinic.dto.veterinary;

import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VeterinaryResponseDTO(
        Long id,
        String name,
        String crmv,
        String contact,
        Specialty specialty,
        boolean active
) {
}
