package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.AgendamentoRequestDTO;
import com.example.AgendamentoMedico.dtos.AgendamentoResponseDTO;
import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.example.AgendamentoMedico.mappers.AgendamentoMapper;
import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.services.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendas")
@Tag(name = "Agendas", description = "Gerenciamento de agendamentos de consultas médicas")
public class AgendaController {

    private final AgendaService service;
    private final AgendamentoMapper mapper;

    public AgendaController(AgendaService service, AgendamentoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista todos os agendamentos",
            description = "Retorna todos os agendamentos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso")
    // Delegamos a busca e a conversão para o service e para o mapper para manter o controller fino
    // e garantir que as regras de negócio e de apresentação fiquem concentradas em suas camadas.
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {
        var list = service.listar().stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um agendamento por ID")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    // Buscamos o agendamento pelo service para reutilizar a validação de existência e depois
    // convertê-lo para DTO, assegurando consistência de saída em toda a aplicação.
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        Agenda a = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(a));
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agenda uma nova consulta",
            description = "Cria um novo agendamento vinculando paciente, médico e data da consulta.")
    @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos parâmetros")
    // Reaproveitamos o serviço para orquestrar a criação do agendamento porque ele valida a
    // disponibilidade de recursos e garante integridade, convertendo o resultado para DTO no retorno.
    public ResponseEntity<AgendamentoResponseDTO> agendar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        var agendamento = service.agendar(
                dto.getPacienteId(),
                dto.getMedicoId(),
                dto.getDataHora(),
                dto.getTipoConsulta()
        );
        return ResponseEntity.ok(mapper.toResponse(agendamento));
    }

    @PutMapping("/{id}/remarcar")
    @Operation(summary = "Remarca um agendamento existente")
    @ApiResponse(responseCode = "200", description = "Agendamento remarcado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    // Utilizamos o service para centralizar as regras de remarcação e convertendo a data recebida
    // em string para LocalDateTime aqui para manter o contrato da API simples ao consumidor.
    public ResponseEntity<Agenda> remarcar(@PathVariable Long id,
                                           @RequestParam String novaDataHora,
                                           @RequestParam TipoConsulta tipoConsulta) {

        LocalDateTime data = LocalDateTime.parse(novaDataHora);
        Agenda agendamento = service.remarcar(id, data, tipoConsulta);
        return ResponseEntity.ok(agendamento);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancela um agendamento",
            description = "Marca o agendamento como cancelado sem removê-lo do banco de dados.")
    @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    // Chamamos o método de cancelamento do service para que todo o fluxo de atualização de status
    // aconteça em um único ponto, retornando o objeto atualizado para feedback imediato ao cliente.
    public ResponseEntity<Agenda> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um agendamento",
            description = "Exclui permanentemente um agendamento.")
    @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    // Delegamos a exclusão ao service para aproveitar validações centralizadas e retornamos 204
    // para seguir a convenção REST de remoções sem conteúdo.
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}