package com.jamillyferreira.veterinaryclinic.service;

import com.jamillyferreira.veterinaryclinic.dto.pet.PetCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Pet;
import com.jamillyferreira.veterinaryclinic.entity.Tutor;
import com.jamillyferreira.veterinaryclinic.exception.BusinessException;
import com.jamillyferreira.veterinaryclinic.exception.ResourceNotFoundException;
import com.jamillyferreira.veterinaryclinic.mapper.PetMapper;
import com.jamillyferreira.veterinaryclinic.repository.ConsultationRepository;
import com.jamillyferreira.veterinaryclinic.repository.PetRepository;
import com.jamillyferreira.veterinaryclinic.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final ConsultationRepository consultationRepository;
    private final PetMapper mapper;

    public PetResponseDTO create(PetCreateDTO dto) {
        log.info("Criando novo pet");
        Tutor existingTutor = tutorRepository.findById(dto.tutorId())
                .orElseThrow(() -> {
                    log.error("Tutor não encontrado com id: {}", dto.tutorId());
                    return new ResourceNotFoundException("Tutor não encontrado com id: " + dto.tutorId());
                });

        Pet pet = mapper.toEntity(dto);
        pet.setTutor(existingTutor);

        Pet savedPet = petRepository.save(pet);

        log.info("Pet criado com sucesso - ID: {}, Nome: {}", savedPet.getId(), savedPet.getName());
        return mapper.toDTO(savedPet);
    }

    public PetResponseDTO findById(Long id) {
        log.info("Buscando pet por ID: {}", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pet não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Pet não encontrado com ID: " + id);
                });

        log.info("Pet encontrado - ID: {}, Nome: {}", pet.getId(), pet.getName());
        return mapper.toDTO(pet);
    }


    public List<PetResponseDTO> findAll() {
        log.info("Buscando todos os pets");
        List<Pet> pets = petRepository.findAll();

        log.info("Pets encontrados: {}", pets.size());
        return pets.stream().map(mapper::toDTO).toList();
    }

    public List<PetResponseDTO> findByTutorId(Long tutorId) {
        log.info("Buscando pets por tutor ID: {}", tutorId);

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado"));

        List<Pet> pets = petRepository.findByTutorId(tutor.getId());

        log.info("Pets encontrados para tutor {}: {}", tutorId, pets.size());
        return pets.stream().map(mapper::toDTO).toList();
    }

    public PetResponseDTO update(Long id, PetUpdateDTO dto) {
        log.info("Atualizando pet - ID: {}", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pet não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Pet não encontrado com ID: " + id);
                });

        mapper.updateEntity(pet, dto);
        Pet updatedPet = petRepository.save(pet);

        log.info("Pet atualizado com sucesso");
        return mapper.toDTO(updatedPet);
    }

    public void delete(Long id) {
        log.info("Deletando pet por id: {}", id);

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pet não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Pet não encontrado com ID: " + id);
                });

        if (consultationRepository.existsByPetId(id)) {
            log.error("Pet possui consultas registradas - ID: {}", id);
            throw new BusinessException("Pet não pode ser removido pois possui consultadas registradas.");
        }
        petRepository.delete(pet);
        log.info("Pet deletado com sucesso - ID: {}", id);
    }


}
