package com.jamillyferreira.veterinaryclinic.dto.veterinary;

import com.jamillyferreira.veterinaryclinic.enums.Specialty;

public record VeterinarySummaryDTO(
        Long id,
        String name,
        Specialty specialty
) {
}
