package com.jamillyferreira.veterinaryclinic.controller;

import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentStatusUpdateDTO;
import com.jamillyferreira.veterinaryclinic.dto.appointment.AppointmentSummaryDTO;
import com.jamillyferreira.veterinaryclinic.enums.AppointmentStatus;
import com.jamillyferreira.veterinaryclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
@Tag(name = "Consulta", description = "Gerenciamento de Appointment")
public class AppointmentController {
    private final AppointmentService service;

    @Operation(summary = "Criar consulta",
            description = "Agenda uma nova consulta. " +
                    "Não é permitido agendar dois atendimentos com o mesmo veterinário no mesmo horário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Já existe consulta para esse horário com veterinário informado"),
            @ApiResponse(responseCode = "404", description = "Veterinário ou Pet não encontrado")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(
            @Parameter(description = "Dados para agendamento da consulta")
            @Valid @RequestBody AppointmentCreateDTO dto) {
        AppointmentResponseDTO appointment = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @Operation(summary = "Listar todas as consultas",
            description = "Retorna todas as consultas. Pode filtrar por petId ou veterinaryId. Nunca ambos simultaneamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "petId e veterinaryId informados simultaneamente"),
            @ApiResponse(responseCode = "404", description = "ID informado não encontrado")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAll(
            @Parameter(description = "Filtra consultas pelo ID do pet. Não pode ser usado junto com veterinaryId.")
            @RequestParam(required = false) Long petId,

            @Parameter(description = "Filtra consultas pelo ID do veterinário. Não pode ser usado junto com petId.")
            @RequestParam(required = false) Long veterinaryId,

            @RequestParam(required = false) AppointmentStatus status) {

        if (petId != null && veterinaryId != null) {
            throw new IllegalArgumentException("Informe apenas petId ou veterinaryId, não ambos.");
        }

        if (petId != null) return ResponseEntity.ok(service.findByPetId(petId));
        if (veterinaryId != null) return ResponseEntity.ok(service.findByVeterinaryId(veterinaryId));

        if (status != null) return ResponseEntity.ok(service.findByStatus(status));

        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(
            @Parameter(description = "ID da consulta", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar consultas por status",
            description = "Retorna todas as consultas com o status informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Valor do status inválido")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> findByStatus(
            @Parameter(description = "Status da consulta")
            @PathVariable AppointmentStatus status) {

        return ResponseEntity.ok(service.findByStatus(status));
    }

    @Operation(summary = "Alterar status da consulta",
            description = "Atualiza o status da consulta respeitando as transições permitidas")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @Parameter(description = "ID da consulta a ter o status alterado")
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStatusUpdateDTO request) {
        return ResponseEntity.ok(service.updateStatus(id, request.status()));
    }

    @Operation(summary = "Cancelar uma consulta",
            description = "A consulta só pode ser cancelada se o status for AGENDADA." +
                    "Status CONCLUIDA e EM_ATENDIMENTO não permitem cancelamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Consulta não pode ser cancelada se status for agendada")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentSummaryDTO> cancel(
            @Parameter(description = "ID da consulta para cancelamento")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.cancelAppointment(id));
    }
}
