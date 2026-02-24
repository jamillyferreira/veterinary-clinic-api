package com.jamillyferreira.veterinaryclinic.mapper;

import com.jamillyferreira.veterinaryclinic.dto.pet.PetCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {
    public Pet toEntity(PetCreateDTO dto) {
        Pet pet = new Pet();
        pet.setName(dto.name());
        pet.setSpecies(dto.species());
        pet.setRace(dto.race());
        pet.setWeight(dto.weight());
        pet.setDateBirth(dto.dateBirth());
        return pet;
    }

    public PetResponseDTO toDTO(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getRace(),
                pet.getWeight(),
                pet.getDateBirth(),
                pet.getTutor().getId(),
                pet.getTutor().getName()
        );
    }

    public void updateEntity(Pet pet, PetUpdateDTO dto) {
        if (dto.name() != null) pet.setName(dto.name());
        if (dto.species() != null) pet.setSpecies(dto.species());
        if (dto.race() != null) pet.setRace(dto.race());
        if (dto.weight() != null) pet.setWeight(dto.weight());
        if (dto.dateBirth() != null) pet.setDateBirth(dto.dateBirth());
    }

}
