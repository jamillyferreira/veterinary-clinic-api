package com.jamillyferreira.veterinaryclinic.service;

import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentSummaryDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
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

        OffsetDateTime scheduledAt = dto.scheduledAt();
        OffsetDateTime end = scheduledAt.plusMinutes(APPOINTMENT_DURATION_MINUTES);

        if (!scheduledAt.isAfter(OffsetDateTime.now())) {
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

    public List<AppointmentResponseDTO> findAll() {
        log.info("Buscando todas as consultas");
        List<Appointment> appointments = appointmentRepository.findAll();

        log.info("Consultas encontradas - Quantidade: {}", appointments.size());
        return appointments.stream().map(mapper::toDTO).toList();
    }

    public List<AppointmentResponseDTO> findByPetId(Long petId) {
        log.info("Buscando consultas por Pet com ID: {}", petId);

        if (!petRepository.existsById(petId)) {
            log.error("Pet não encontrado com ID: {}", petId);
            throw new ResourceNotFoundException("Pet não encontrado com ID: " + petId);
        }

        List<Appointment> appointments = appointmentRepository.findByPetId(petId);

        return appointments.stream().map(mapper::toDTO).toList();
    }

    public List<AppointmentResponseDTO> findByVeterinaryId(Long veterinaryId) {
        log.info("Buscando consultas por veterinário com ID: {}", veterinaryId);

        if (!veterinaryRepository.existsById(veterinaryId)) {
            log.error("Veterinário não encontrado com ID: {}", veterinaryId);
            throw new ResourceNotFoundException("Veterinário não encontrado com ID: " + veterinaryId);
        }

        List<Appointment> appointments = appointmentRepository.findByVeterinaryId(veterinaryId);

        return appointments.stream().map(mapper::toDTO).toList();
    }

    public List<AppointmentResponseDTO> findByStatus(AppointmentStatus status) {
        log.info("Buscando consultas por status: {}", status);

        List<AppointmentResponseDTO> appointments = appointmentRepository.findByStatus(status)
                .stream()
                .map(mapper::toDTO)
                .toList();

        log.info("Consultas encontradas - Quantidade: {}", appointments.size());
        return appointments;
    }

    public AppointmentResponseDTO findById(Long id) {
        log.info("Buscando consultas por ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Consulta não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Consulta não encontrado com ID: " + id);
                });

        log.info("Consulta retornada - ID: {}", appointment.getId());
        return mapper.toDTO(appointment);

    }

    public AppointmentResponseDTO updateStatus(Long id, AppointmentStatus newStatus) {
        log.info("Atualizando status da consulta id: {} para {}", id, newStatus);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Consulta não encontrada com id: " + id);
                    return new ResourceNotFoundException("Consulta não encontrada com id: " + id);
                });

        AppointmentStatus currentStatus = appointment.getStatus();

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(String.format(
                    "Transição inválida: %s -> %s. Transições permitidas: %s",
                    currentStatus, newStatus, currentStatus.nextAllowed()
            ));
        }

        appointment.setStatus(newStatus);
        Appointment updated = appointmentRepository.save(appointment);

        log.info("Status atualizado com sucesso - ID: {} {} -> {}", id, currentStatus, newStatus);
        return mapper.toDTO(updated);
    }

    public AppointmentSummaryDTO cancelAppointment(Long id) {
        log.info("Cancelando consulta com ID: {}", id);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Consulta não encontrada com ID: {}", id);
                    return new ResourceNotFoundException("Consulta não encontrada com ID: " + id);
                });

        AppointmentStatus currentStatus = appointment.getStatus();

        if (!currentStatus.canTransitionTo(AppointmentStatus.CANCELADA)) {
            throw new BusinessException("Consulta não pode ser cancelada. Status atual: " + currentStatus);
        }

        appointment.setStatus(AppointmentStatus.CANCELADA);
        Appointment updated = appointmentRepository.save(appointment);

        log.info("Consulta cancelada com sucesso - ID: {}", id);
        return mapper.toSummaryDTO(updated);
    }

}
