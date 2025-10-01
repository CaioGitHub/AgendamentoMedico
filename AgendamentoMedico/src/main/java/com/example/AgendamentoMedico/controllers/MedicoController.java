package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.MedicoRequestDTO;
import com.example.AgendamentoMedico.dtos.MedicoResponseDTO;
import com.example.AgendamentoMedico.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<MedicoResponseDTO>> buscarPorEspecialidade(@PathVariable String especialidade) {
        return ResponseEntity.ok(medicoService.buscarPorEspecialidade(especialidade));
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> criar(@RequestBody MedicoRequestDTO medicoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.salvar(medicoRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody MedicoRequestDTO medicoRequestDTO) {
        return ResponseEntity.ok(medicoService.atualizar(id, medicoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
