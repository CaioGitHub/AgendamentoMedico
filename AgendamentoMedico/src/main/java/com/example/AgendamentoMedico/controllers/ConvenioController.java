package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.ConvenioDTO;
import com.example.AgendamentoMedico.services.ConvenioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/convenios")
@RequiredArgsConstructor
public class ConvenioController {

    private final ConvenioService service;

    @GetMapping
    public ResponseEntity<List<ConvenioDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvenioDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ConvenioDTO> salvar(@RequestBody ConvenioDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConvenioDTO> atualizar(@PathVariable Long id,
                                                 @RequestBody ConvenioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
