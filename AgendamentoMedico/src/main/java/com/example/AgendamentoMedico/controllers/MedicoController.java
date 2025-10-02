package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.MedicoRequestDTO;
import com.example.AgendamentoMedico.dtos.MedicoResponseDTO;
import com.example.AgendamentoMedico.services.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Médicos", description = "Gerenciamento de médicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    @Operation(summary = "Lista todos os médicos",
            description = "Retorna uma lista de todos os médicos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso")
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null && size == null) {
            List<MedicoResponseDTO> medicos = medicoService.listarTodos();
            return ResponseEntity.ok(medicos);
        }

        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 4;

        Page<MedicoResponseDTO> medicos = medicoService.listarTodos(pageNumber, pageSize);
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca médico por ID",
            description = "Retorna os dados de um médico específico pelo ID informado.")
    @ApiResponse(responseCode = "200", description = "Médico encontrado")
    @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Busca médicos por especialidade",
            description = "Retorna todos os médicos que possuem a especialidade informada.")
    @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum médico encontrado para a especialidade")
    public ResponseEntity<Page<MedicoResponseDTO>> listarPorEspecialidade(
            @PathVariable String especialidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        return ResponseEntity.ok(medicoService.listarPorEspecialidade(especialidade, page, size));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo médico",
            description = "Cria um novo registro de médico no sistema.")
    @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do médico")
    public ResponseEntity<MedicoResponseDTO> criar(@RequestBody MedicoRequestDTO medicoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(medicoRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um médico existente",
            description = "Atualiza os dados de um médico já cadastrado.")
    @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados")
    @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody MedicoRequestDTO medicoRequestDTO) {
        return ResponseEntity.ok(medicoService.atualizar(id, medicoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um médico",
            description = "Remove permanentemente um médico pelo ID informado.")
    @ApiResponse(responseCode = "204", description = "Médico excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
