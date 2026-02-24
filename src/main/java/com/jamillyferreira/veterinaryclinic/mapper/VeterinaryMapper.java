package com.jamillyferreira.veterinaryclinic.mapper;

import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import org.springframework.stereotype.Component;

@Component
public class VeterinaryMapper {

    public Veterinary toEntity(VeterinaryCreateDTO dto) {
        Veterinary veterinary = new Veterinary();
        veterinary.setName(dto.name());
        veterinary.setCrmv(dto.crmv());
        veterinary.setContact(dto.contact());
        veterinary.setSpecialty(dto.specialty());

        return veterinary;
    }

    public VeterinaryResponseDTO toDTO(Veterinary veterinary) {
        return new VeterinaryResponseDTO(
                veterinary.getId(),
                veterinary.getName(),
                veterinary.getCrmv(),
                veterinary.getContact(),
                veterinary.getSpecialty(),
                veterinary.isActive()
        );
    }

    public void updateEntity(Veterinary veterinary, VeterinaryUpdateDTO dto) {
        if (dto.name() != null) veterinary.setName(dto.name());
        if (dto.contact() != null) veterinary.setContact(dto.contact());
        if (dto.crmv() != null) veterinary.setCrmv(dto.crmv());
        if (dto.specialty() != null) veterinary.setSpecialty(dto.specialty());

    }

}
