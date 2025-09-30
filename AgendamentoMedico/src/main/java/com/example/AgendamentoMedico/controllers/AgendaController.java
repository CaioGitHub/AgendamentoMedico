package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.services.AgendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    private final AgendaService service;

    public AgendaController(AgendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Agenda> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/agendar")
    public ResponseEntity<Agenda> agendar(@RequestParam Long pacienteId,
                                          @RequestParam Long medicoId,
                                          @RequestParam String dataHora,
                                          @RequestParam TipoConsulta tipoConsulta) {

        LocalDateTime data = LocalDateTime.parse(dataHora); // formato ISO: 2025-10-12T14:00
        Agenda agendamento = service.agendar(pacienteId, medicoId, data, tipoConsulta);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}/remarcar")
    public ResponseEntity<Agenda> remarcar(@PathVariable Long id,
                                           @RequestParam String novaDataHora,
                                           @RequestParam TipoConsulta tipoConsulta) {

        LocalDateTime data = LocalDateTime.parse(novaDataHora);
        Agenda agendamento = service.remarcar(id, data, tipoConsulta);
        return ResponseEntity.ok(agendamento);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agenda> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}