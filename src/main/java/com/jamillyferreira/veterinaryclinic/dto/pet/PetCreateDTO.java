package com.jamillyferreira.veterinaryclinic.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PetCreateDTO(
        @NotBlank(message = "Nome do pet é obrigatório")
        @Size(min = 2, max = 75, message = "Entre 2 e 75 caracteres")
        String name,

        @NotBlank(message = "Especie é obrigatória")
        String species,

        String race,

        @Positive(message = "Peso do pet deve ser maior que zero")
        Double weight,

        LocalDate dateBirth,

        @NotNull(message = "Tutor é obrigatório")
        Long tutorId
) {
}
