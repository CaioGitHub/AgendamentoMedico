package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.MedicoDTO;
import com.example.AgendamentoMedico.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<MedicoDTO>> buscarPorEspecialidade(@PathVariable String especialidade) {
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(especialidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
