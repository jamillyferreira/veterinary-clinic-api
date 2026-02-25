package com.jamillyferreira.veterinaryclinic.controller;

import com.jamillyferreira.veterinaryclinic.dto.pet.PetCreateDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetResponseDTO;
import com.jamillyferreira.veterinaryclinic.dto.pet.PetUpdateDTO;
import com.jamillyferreira.veterinaryclinic.exception.ErrorResponse;
import com.jamillyferreira.veterinaryclinic.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/pets")
@Tag(name = "Pets", description = "Gerenciamente de Pets")
public class PetController {
    private final PetService petService;

    @Operation(summary = "Criar pet",
            description = "Cria um novo pet vinculado a um tutor existente." +
                    "O tutor deve estar previamente cadastrado no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação no corpo da requisição"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @PostMapping
    public ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.create(dto));
    }

    @Operation(summary = "Listar todos os pets",
            description = "Retorna uma lista completa de pets cadastrados no sistema. " +
                    "Pode filtrar pelos pets de um tutor específico passando o tutorId como parâmetro opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pets retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> findAll(
            @Parameter(description = "ID do Tutor para filtar (Opcional). Se não informado, retorna todos os pets",
                    example = "1")
            @RequestParam(required = false) Long tutorId) {

        if (tutorId != null) {
            return ResponseEntity.ok(petService.findByTutorId(tutorId));
        }
        return ResponseEntity.ok(petService.findAll());
    }

    @Operation(summary = "Buscar pet por ID",
            description = "Retorna os dados de um pet específico com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")

    })
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> findById(
            @Parameter(description = "ID do Pet", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @Operation(summary = "Atualizar pet parcialmente")
    @PatchMapping("/{id}")
    public ResponseEntity<PetResponseDTO> update(
            @Parameter(description = "ID do Pet", required = true)
            @PathVariable Long id, @Valid @RequestBody PetUpdateDTO dto) {
        return ResponseEntity.ok(petService.update(id, dto));
    }

    @Operation(summary = "Deleter pet por ID", description = "Remove um pet do sistema baseado pelo ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do Pet") @PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
