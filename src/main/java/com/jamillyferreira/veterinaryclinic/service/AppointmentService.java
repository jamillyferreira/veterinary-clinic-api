package com.jamillyferreira.veterinaryclinic.service;

import com.jamillyferreira.veterinaryclinic.dto.appointment.*;
import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import com.jamillyferreira.veterinaryclinic.entity.Pet;
import com.jamillyferreira.veterinaryclinic.entity.Veterinary;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import com.jamillyferreira.veterinaryclinic.exception.BusinessException;
import com.jamillyferreira.veterinaryclinic.exception.InvalidStatusTransitionException;
import com.jamillyferreira.veterinaryclinic.exception.ResourceNotFoundException;
import com.jamillyferreira.veterinaryclinic.mapper.AppointmentMapper;
import com.jamillyferreira.veterinaryclinic.repository.AppointmentRepository;
import com.jamillyferreira.veterinaryclinic.repository.PetRepository;
import com.jamillyferreira.veterinaryclinic.repository.VeterinaryRepository;
import com.jamillyferreira.veterinaryclinic.specification.AppointmentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VeterinaryRepository veterinaryRepository;
    private final AppointmentMapper mapper;
    private static final long APPOINTMENT_DURATION_MINUTES = 30;

    @Transactional
    public AppointmentResponseDTO create(AppointmentCreateDTO dto) {

        LocalDateTime scheduledAt = dto.scheduledAt();
        LocalDateTime end = scheduledAt.plusMinutes(APPOINTMENT_DURATION_MINUTES);

        if (!scheduledAt.isAfter(LocalDateTime.now())) {
            throw new BusinessException("A consulta deve ser agendada para uma data futura");
        }

        int minute = scheduledAt.getMinute();
        if (minute != 0 && minute != 30) {
            throw new BusinessException("Horário deve ser de 30 em 30 minutos");
        }

        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pet não encontrado com ID: " + dto.petId())
                );

        Veterinary veterinary = veterinaryRepository.findById(dto.veterinaryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veterinário não encontrado com ID: " + dto.veterinaryId())
                );

        if (!veterinary.isActive()) {
            throw new BusinessException("Não é possível agendar consulta com veterinário inativo");
        }

        boolean existsConflict = appointmentRepository.existsConflictingAppointment(
                veterinary.getId(),
                scheduledAt,
                end
        );

        if (existsConflict) {
            throw new BusinessException("Já existe consulta agendada para esse veterinário nesse horário");
        }

        Appointment appointment = mapper.toEntity(dto);
        appointment.setPet(pet);
        appointment.setVeterinary(veterinary);
        appointment.setStatus(AppointmentStatus.AGENDADA);

        return mapper.toDTO(appointmentRepository.save(appointment));

    }

    public List<AppointmentResponseDTO> findAll(
            Long petId,
            Long veterinaryId,
            AppointmentStatus status
    ) {
            log.info("Buscando consultas");

            if (petId != null && !petRepository.existsById(petId)) {
                throw new ResourceNotFoundException("Pet não encontrado com ID: " + petId);
            }

            if (veterinaryId != null && !veterinaryRepository.existsById(veterinaryId)) {
                throw new ResourceNotFoundException("Veterinário não encontrado com ID: " + veterinaryId);
            }

            Specification<Appointment> spec = AppointmentSpecification.withFilters(petId, veterinaryId, status);
            List<Appointment> appointments = appointmentRepository.findAll(spec);

            log.info("Consultas encontradas - Quantidade: {}", appointments.size());
            return appointments.stream().map(mapper::toDTO).toList();
    }

    public AppointmentDetailsDTO findById(Long id) {
        log.info("Buscando consultas por ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Consulta não encontrado com ID: " + id);
                });

        log.info("Consulta retornada - ID: {}", appointment.getId());
        return mapper.toDetailsDTO(appointment);

    }

    public AppointmentResponseDTO updateStatus(Long id, AppointmentStatusUpdateDTO dto) {
        log.info("Atualizando status da consulta id: {} para {}", id, dto.status());

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrada com id: {}", id);
                    return new ResourceNotFoundException("Consulta não encontrada com id: " + id);
                });

        AppointmentStatus currentStatus = appointment.getStatus();

        if (!currentStatus.canTransitionTo(dto.status())) {
            throw new InvalidStatusTransitionException("Transição inválida");
        }

        appointment.setStatus(dto.status());
        Appointment updated = appointmentRepository.save(appointment);

        log.info("Status atualizado com sucesso - ID: {} {} -> {}", id, currentStatus, dto.status());
        return mapper.toDTO(updated);
    }

    @Transactional
    public AppointmentDetailsDTO concludeAppointment(Long id, ClinicalDataInputDTO dto) {
        log.info("Adicionando diagnóstico para consulta por ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta não encontrada com ID: {}", id);
                    return new ResourceNotFoundException("Consulta não encontrada com ID: " + id);
                });

        if (appointment.getStatus() != AppointmentStatus.EM_ATENDIMENTO) {
            log.warn("Tentativa de adicionar diagnóstico em consulta com status: {}", appointment.getStatus());
            throw new BusinessException("Status atual não permite adição de diagnóstico.");
        }

        appointment.setDiagnosis(dto.diagnosis());
        appointment.setObservations(dto.observations());
        appointment.setStatus(AppointmentStatus.CONCLUIDA);

        Appointment updated = appointmentRepository.save(appointment);
        log.info("Diagnóstico registrado - consulta ID: {} concluída", id);

        return mapper.toDetailsDTO(updated);
    }


}
