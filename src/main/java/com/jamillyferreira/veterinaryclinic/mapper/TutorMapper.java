package com.jamillyferreira.veterinaryclinic.mapper;


import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Tutor;
import org.springframework.stereotype.Component;

@Component
public class TutorMapper {

    public Tutor toEntity(TutorCreateDTO dto) {
        Tutor tutor = new Tutor();
        tutor.setName(dto.name());
        tutor.setCpf(normalizeCpf(dto.cpf()));
        tutor.setContact(dto.contact());
        tutor.setEmail(dto.email());

        return tutor;
    }

    public TutorResponseDTO toDTO(Tutor tutor) {
        return new TutorResponseDTO(
                tutor.getId(),
                tutor.getName(),
                tutor.getCpf(),
                tutor.getContact(),
                tutor.getEmail()
        );
    }

    public void updateEntity(Tutor tutor, TutorUpdateDTO dto) {
        if (dto.name() != null) tutor.setName(dto.name());
        if (dto.contact() != null) tutor.setContact(dto.contact());
        if (dto.email() != null) tutor.setEmail(dto.email());
    }

    public String normalizeCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }
}
