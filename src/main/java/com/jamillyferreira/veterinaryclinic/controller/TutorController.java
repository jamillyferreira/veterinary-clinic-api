package com.jamillyferreira.veterinaryclinic.controller;

import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.tutor.TutorUpdateDTO;
import com.jamillyferreira.veterinaryclinic.service.TutorService;
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
@RequestMapping("/tutors")
@Tag(name = "Tutores", description = "Gereciamento de tutores")
public class TutorController {
    private final TutorService service;

    @Operation(summary = "Criar tutor", description = "Cadastra um novo tutor no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor cadastrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito. CPF ou E-mail já cadastrados no sistema"),
            @ApiResponse(responseCode = "400", description = "Erro de validação na requsição"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor"),
    })
    @PostMapping
    public ResponseEntity<TutorResponseDTO> create(@Valid @RequestBody TutorCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Buscar tutor por id", description = "Retorna um tutor cadastrado no sistema por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> findById(
            @Parameter(description = "ID do tutor", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar tutores", description = "Retorna uma lista com tutores cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutores encontrados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar tutor por CPF",
            description = "Retorna um tutor cadastrado no sistema a partir do CPF informado." +
                    "O CPF pode ser informado com ou sem máscara.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado com o CPF informado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<TutorResponseDTO> findByCpf(
            @Parameter(description = "CPF do tutor (com ou sem máscara)", required = true)
            @PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @Operation(summary = "Atualizar tutor parcialmente", description = "Atualiza os dados de um tutor pelo ID informado." +
            "Apenas os campos enviados no corpo da requisição serão atualizados.")
    @PatchMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> update(
            @Parameter(description = "ID do tutor", required = true)
            @PathVariable Long id, @Valid @RequestBody TutorUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Deletat tutor", description = "Remove um tutor pelo ID informado. " +
            "A exclusão só é permitida caso o tutor não possua pets cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado"),
            @ApiResponse(responseCode = "422", description = "Não é possível excluir tutor com pets cadastrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do tutor", required = true)
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
