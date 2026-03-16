package com.jamillyferreira.veterinaryclinic.mapper;

import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentSummaryDTO;
import com.jamillyferreira.veterinaryclinic.entity.Appointment;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {
    private final PetMapper petMapper;
    private final VeterinaryMapper veterinaryMapper;

    public Appointment toEntity(AppointmentCreateDTO dto) {
        Appointment appointment = new Appointment();

        appointment.setScheduledAt(dto.scheduledAt());
        appointment.setReason(dto.reason());
        appointment.setStatus(AppointmentStatus.AGENDADA);

        return appointment;
    }

    public AppointmentSummaryDTO toSummaryDTO(Appointment appointment) {
        return new AppointmentSummaryDTO(
                appointment.getId(),
                appointment.getStatus(),
                OffsetDateTime.now()
        );
    }

    public AppointmentResponseDTO toDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                petMapper.toSummaryDTO(appointment.getPet()),
                veterinaryMapper.toSummaryDTO(appointment.getVeterinary()),
                appointment.getReason(),
                appointment.getStatus(),
                appointment.getScheduledAt()
        );
    }
}
