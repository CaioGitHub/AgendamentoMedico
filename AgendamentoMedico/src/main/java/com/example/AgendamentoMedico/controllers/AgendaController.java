package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.services.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendas")
@Tag(name = "Agendas", description = "Gerenciamento de agendamentos de consultas médicas")
public class AgendaController {

    private final AgendaService service;

    public AgendaController(AgendaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os agendamentos",
            description = "Retorna todos os agendamentos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso")
    public List<Agenda> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um agendamento por ID")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agenda uma nova consulta",
            description = "Cria um novo agendamento vinculando paciente, médico e data da consulta.")
    @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos parâmetros")
    public ResponseEntity<Agenda> agendar(@RequestParam Long pacienteId,
                                          @RequestParam Long medicoId,
                                          @RequestParam String dataHora,
                                          @RequestParam TipoConsulta tipoConsulta) {

        LocalDateTime data = LocalDateTime.parse(dataHora); // formato ISO: 2025-10-12T14:00
        Agenda agendamento = service.agendar(pacienteId, medicoId, data, tipoConsulta);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/remarcar")
    @Operation(summary = "Remarca um agendamento existente")
    @ApiResponse(responseCode = "200", description = "Agendamento remarcado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
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
    public ResponseEntity<Agenda> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um agendamento",
            description = "Exclui permanentemente um agendamento.")
    @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}