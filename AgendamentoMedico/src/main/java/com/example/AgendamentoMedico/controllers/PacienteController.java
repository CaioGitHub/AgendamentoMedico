package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.mappers.PacienteMapper;
import com.example.AgendamentoMedico.dtos.PacienteRequestDTO;
import com.example.AgendamentoMedico.dtos.PacienteResponseDTO;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.models.Paciente;
import com.example.AgendamentoMedico.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    @Autowired
    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAllPacientes() {
        List<PacienteResponseDTO> pacientes = pacienteService.findAll()
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + id));
        return ResponseEntity.ok(pacienteMapper.toResponse(paciente));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PacienteResponseDTO> getPacienteByEmail(@PathVariable String email) {
        Paciente paciente = pacienteService.findByEmail(email);
        return ResponseEntity.ok(pacienteMapper.toResponse(paciente));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PacienteResponseDTO>> searchPacientesByNome(@RequestParam String nome) {
        List<PacienteResponseDTO> pacientes = pacienteService.findByNomeContaining(nome)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO request) {
        Paciente paciente = pacienteMapper.toEntity(request);
        Paciente savedPaciente = pacienteService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteMapper.toResponse(savedPaciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequestDTO request) {

        Paciente pacienteAtualizado = pacienteMapper.toEntity(request);
        pacienteAtualizado.setId(id);

        Paciente updatedPaciente = pacienteService.update(id, pacienteAtualizado);
        return ResponseEntity.ok(pacienteMapper.toResponse(updatedPaciente));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> partialUpdatePaciente(
            @PathVariable Long id,
            @RequestBody PacienteRequestDTO request) {

        Paciente pacienteExistente = pacienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + id));

        pacienteMapper.updateEntityFromRequest(request, pacienteExistente);
        Paciente updatedPaciente = pacienteService.save(pacienteExistente);

        return ResponseEntity.ok(pacienteMapper.toResponse(updatedPaciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}