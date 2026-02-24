package com.jamillyferreira.veterinaryclinic.service;

import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import com.jamillyferreira.veterinaryclinic.exception.BusinessException;
import com.jamillyferreira.veterinaryclinic.exception.DuplicateResourceException;
import com.jamillyferreira.veterinaryclinic.exception.ResourceNotFoundException;
import com.jamillyferreira.veterinaryclinic.mapper.VeterinaryMapper;
import com.jamillyferreira.veterinaryclinic.repository.VeterinaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VeterinaryService {
    private final VeterinaryRepository repository;
    private final VeterinaryMapper mapper;

    public VeterinaryResponseDTO create(VeterinaryCreateDTO dto) {
        log.info("Criando novo veterinário: {}", dto.name());

        if (repository.existsByCrmv(dto.crmv())) {
            throw new DuplicateResourceException("CRMV já cadastrado: " + dto.crmv());
        }

        Veterinary newVeterinary = mapper.toEntity(dto);
        Veterinary savedVeterinary = repository.save(newVeterinary);

        log.info("Veterinário criado com sucesso. ID: {}", savedVeterinary.getId());
        return mapper.toDTO(savedVeterinary);
    }

    public List<VeterinaryResponseDTO> findAll(Specialty specialty) {
        log.info("Buscando todos os veterinários");
        List<Veterinary> veterinaries;
        if (specialty != null) {
            log.info("Buscando veterinários por especialidade");
            veterinaries = repository.findBySpecialty(specialty);
        } else {
            veterinaries = repository.findAll();
        }
        return veterinaries.stream().map(mapper::toDTO).toList();
    }

    public VeterinaryResponseDTO findById(Long id) {
        log.info("Buscando veterinário por ID: {}", id);
        Veterinary veterinary = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com ID: " + id));

        log.info("Veteribário encontrado com sucesso");
        return mapper.toDTO(veterinary);
    }

    public VeterinaryResponseDTO update(Long id, VeterinaryUpdateDTO dto) {
        log.info("Atualizado veterinário ID: {}", id);
        Veterinary veterinary = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com ID: " + id)
                );

        mapper.updateEntity(veterinary, dto);
        Veterinary updatedVeterinary = repository.save(veterinary);

        log.info("Veterinário atualizado com sucesso");
        return mapper.toDTO(updatedVeterinary);
    }

    public void deactivate(Long id) {
        Veterinary veterinary = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com ID: " + id)
                );
        if (!veterinary.isActive()) {
            throw new BusinessException("Veterinário já está inativo");
        }
        veterinary.setActive(false);
    }

    public void activate(Long id) {
        Veterinary veterinary = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com ID: " + id)
                );
        if (veterinary.isActive()) {
            throw new BusinessException("Veterinário já está ativo");
        }
        veterinary.setActive(true);
    }

}
