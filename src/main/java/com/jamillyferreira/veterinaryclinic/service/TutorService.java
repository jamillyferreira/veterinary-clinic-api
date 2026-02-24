package com.jamillyferreira.veterinaryclinic.service;

import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorUpdateDTO;
import com.jamillyferreira.veterinaryclinic.entity.Tutor;
import com.jamillyferreira.veterinaryclinic.exception.BusinessException;
import com.jamillyferreira.veterinaryclinic.exception.ResourceNotFoundException;
import com.jamillyferreira.veterinaryclinic.mapper.TutorMapper;
import com.jamillyferreira.veterinaryclinic.repository.PetRepository;
import com.jamillyferreira.veterinaryclinic.repository.TutorRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final TutorMapper mapper;

    public TutorResponseDTO create(TutorCreateDTO dto) {
        log.info("Criando um novo tutor: {}", dto.name());

        Tutor newTutor = mapper.toEntity(dto);
        Tutor savedTutor = tutorRepository.save(newTutor);

        log.info("Tutor cadastrado com sucesso");
        return mapper.toDTO(savedTutor);
    }

    public TutorResponseDTO findById(Long id) {
        log.info("Buscando tutor por ID: {}", id);
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com ID: " + id));

        log.info("Tutor encontrado com sucesso");
        return mapper.toDTO(tutor);
    }

    public List<TutorResponseDTO> findAll() {
        log.info("Buscando todos os tutores");
        List<Tutor> tutors = tutorRepository.findAll();

        log.info("Tutores encontrados - Quantidade: {}", tutors.size());
        return tutors.stream().map(mapper::toDTO).toList();
    }

    public TutorResponseDTO findByCpf(String cpf) {
        log.info("Buscando tutor por CPF");
        String normalizedCpf = mapper.normalizeCpf(cpf);

        Tutor tutor = tutorRepository.findByCpf(normalizedCpf)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado")
                );

        log.info("Tutor encontrado com sucesso");
        return mapper.toDTO(tutor);
    }

    public TutorResponseDTO update(Long id, TutorUpdateDTO dto) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tutor não encontrado com ID: " + id)
                );

        mapper.updateEntity(tutor, dto);
        Tutor updatedTutor = tutorRepository.save(tutor);
        return mapper.toDTO(updatedTutor);
    }

    public void delete(Long id) {
        log.info("Tentando deletar tutor ID: {}", id);

        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Tutor não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Tutor não encontrado com ID: " + id);
                });
        if (petRepository.existsByTutorId(id)) {
            throw new BusinessException("Não é possível excluir tutor com pets cadastrados");
        }
        tutorRepository.delete(tutor);
        log.info("Tutor deletado com sucesso - ID: {}", id);
    }
}
