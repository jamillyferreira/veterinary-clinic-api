package com.jamillyferreira.veterinaryclinic.dto.tutor;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record TutorCreateDTO(
        @NotBlank(message = "Nome do tutor é obrigatório")
        @Size(min = 3, max = 100, message = "Entre 3 e 100 caracteres")
        String name,

        @NotBlank(message = "CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Contato é obrigatório")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "Formato inválido, contato deve conter 11 dígitos númericos. Use 92999999999")
        String contact,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email
) {
}
