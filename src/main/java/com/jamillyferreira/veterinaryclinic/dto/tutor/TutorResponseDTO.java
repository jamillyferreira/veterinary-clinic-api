package com.jamillyferreira.veterinaryclinic.dto.tutor;

public record TutorResponseDTO(
        Long id,
        String name,
        String cpf,
        String contact,
        String email
) {
}
