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

    @Operation(summary = "Criar veterinário", description = "Cadastra um novo veterinário no sistema. " +
            "Retorna os dados do veterinário criado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Erro de sintaxe ou estrutura no corpo da requisição"),
            @ApiResponse(responseCode = "422", description = "Erro de validação ou violação de regra de negócio"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @PostMapping
    public ResponseEntity<VeterinaryResponseDTO> create(@Valid @RequestBody VeterinaryCreateDTO dto) {
        VeterinaryResponseDTO veterinary = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinary);
    }

    @Operation(summary = "Listar veterinários",
            description = "Retorna todos os veterinários cadastrados. " +
                    "Use o parâmetro 'specialty' para filtrar por especialidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @GetMapping
    public ResponseEntity<List<VeterinaryResponseDTO>> findAll(
            @Parameter(description = "Filtrar por especialidade. Exemplo: CARDIOLOGIA")
            @RequestParam(required = false) Specialty specialty) {
        return ResponseEntity.ok(service.findAll(specialty));
    }

    @Operation(summary = "Buscar veterinário por id",
            description = "Retorna um veterinário por id cadastrado no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Atualizar veterinário",
            description = "Atualiza parcialmente dados do veterinário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<VeterinaryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody VeterinaryUpdateDTO dto) {
        VeterinaryResponseDTO veterinary = service.update(id, dto);
        return ResponseEntity.ok(veterinary);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }


}
