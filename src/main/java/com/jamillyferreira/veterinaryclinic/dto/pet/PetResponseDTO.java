package com.jamillyferreira.veterinaryclinic.dto.pet;

import java.time.LocalDate;

public record PetResponseDTO(
        Long id,
        String name,
        String species,
        String race,
        Double weight,
        LocalDate dateBirth,
        Long tutorId,
        String tutorName
) {
}
