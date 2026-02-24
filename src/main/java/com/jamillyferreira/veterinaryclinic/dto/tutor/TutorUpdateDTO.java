package com.jamillyferreira.veterinaryclinic.dto.tutor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TutorUpdateDTO(
        @Size(min = 3, max = 100, message = "Entre 3 e 100 caracteres")
        String name,

        @Pattern(
                regexp = "^\\d{11}$",
                message = "Formato inválido, contato deve conter 11 dígitos númericos. Use 92999999999")
        String contact,

        @Email(message = "E-mail inválido")
        String email
) {
}
