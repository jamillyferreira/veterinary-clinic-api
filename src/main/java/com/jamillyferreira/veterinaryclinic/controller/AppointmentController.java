package com.jamillyferreira.veterinaryclinic.controller;

import com.jamillyferreira.veterinaryclinic.dto.appointment.*;
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
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Veterinário ou Pet não encontrado."),
            @ApiResponse(responseCode = "422", description = "Já existe consulta para esse horário com veterinário informado."),
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(
            @Parameter(description = "Dados para agendamento da consulta")
            @RequestBody @Valid AppointmentCreateDTO dto) {
        AppointmentResponseDTO appointment = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @Operation(summary = "Listar consultas",
            description = "Retorna todas as consultas. Filtros opcionais e combináveis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "ID informado não encontrado.")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAll(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) Long veterinaryId,
            @RequestParam(required = false) AppointmentStatus status) {

        return ResponseEntity.ok(service.findAll(petId, veterinaryId, status));
    }

    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDTO> findById(
            @Parameter(description = "ID da consulta", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Alterar status da consulta",
            description = "Atualiza o status da consulta respeitando as transições permitidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da consulta atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Valor inválido na requisição."),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada."),
            @ApiResponse(responseCode = "422", description = "Transição inválida."),
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @Parameter(description = "ID da consulta a ter o status alterado")
            @PathVariable Long id,
            @RequestBody @Valid AppointmentStatusUpdateDTO dto) {

        return ResponseEntity.ok(service.updateStatus(id, dto));
    }

    @Operation(summary = "Concluir a consulta",
            description = "Registra o diagnóstico e observações clínicas, concluindo a consulta. " +
                    "Só é permitido para consultas com status EM_ATENDIMENTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta concluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada."),
            @ApiResponse(responseCode = "422", description = "Consulta não esta em atendimento.")
    })
    @PatchMapping("/{id}/clinical-data")
    public ResponseEntity<AppointmentDetailsDTO> concludeAppointment(
            @Parameter(description = "ID da consulta a ser concluída", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ClinicalDataInputDTO dto) {

        AppointmentDetailsDTO response = service.concludeAppointment(id, dto);
        return ResponseEntity.ok(response);
    }
}
