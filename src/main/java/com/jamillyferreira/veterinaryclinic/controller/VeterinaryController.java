package com.jamillyferreira.veterinaryclinic.controller;

import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.veterinary.VeterinaryUpdateDTO;
import com.jamillyferreira.veterinaryclinic.enums.Specialty;
import com.jamillyferreira.veterinaryclinic.service.VeterinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.LongFunction;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/veterinarians")
@Tag(name = "Veterinários", description = "Gerenciamento de veterinários")
public class VeterinaryController {
    private final VeterinaryService service;

    @Operation(summary = "Cadastrar veterinário", description = "Cadastra um novo veterinário no sistema. " +
            "Retorna os dados do veterinário criado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação no corpo da requisição."),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou violação de regra de negócio."),
    })
    @PostMapping
    public ResponseEntity<VeterinaryResponseDTO> create(@RequestBody @Valid VeterinaryCreateDTO dto) {
        VeterinaryResponseDTO veterinary = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinary);
    }

    @Operation(summary = "Listar veterinários",
            description = "Retorna todos os veterinários cadastrados. Use o parâmetro 'specialty' para filtrar por especialidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
    })
    @GetMapping
    public ResponseEntity<List<VeterinaryResponseDTO>> findAll(
            @Parameter(description = "Filtrar por especialidade. Exemplo: CARDIOLOGIA")
            @RequestParam(required = false) Specialty specialty) {
        return ResponseEntity.ok(service.findAll(specialty));
    }

    @Operation(summary = "Buscar veterinário",
            description = "Retorna um veterinário por ID cadastrado no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryResponseDTO> findById(
            @Parameter(description = "ID do veterinário", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Atualizar veterinário",
            description = "Atualiza parcialmente dados do veterinário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado."),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<VeterinaryResponseDTO> update(@PathVariable Long id, @RequestBody @Valid VeterinaryUpdateDTO dto) {
        VeterinaryResponseDTO veterinary = service.update(id, dto);
        return ResponseEntity.ok(veterinary);
    }

    @Operation(summary = "Ativar veterinário",
            description = "Ativa um veterinário, permitindo que ele volte a operar no sistema, como receber agendamentos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Veterinário já está ativo."),
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @Parameter(description = "ID do veterinário", example = "1")
            @PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desativar veterinário",
            description = "Desativa um veterinário, impedindo que ele receba novos agendamentos, sem remover seu histórico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Desativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Veterinário já está inativo."),
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "ID do veterinário", example = "1")
            @PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }


}
