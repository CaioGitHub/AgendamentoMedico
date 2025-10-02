package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.PacienteRequestDTO;
import com.example.AgendamentoMedico.dtos.PacienteResponseDTO;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.models.Paciente;
import com.example.AgendamentoMedico.services.PacienteService;
import com.example.AgendamentoMedico.mappers.PacienteMapper;
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
    // Buscamos todos os pacientes via service e usamos o mapper para converter as entidades em DTOs
    // garantindo que o controller apenas coordene o fluxo HTTP sem conhecer a estrutura interna.
    public ResponseEntity<List<PacienteResponseDTO>> getAllPacientes() {
        List<PacienteResponseDTO> pacientes = pacienteService.findAll()
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    // Reutilizamos o service para localizar o paciente e lançamos exceção padronizada quando não
    // encontrado, permitindo que o controller devolva a resposta já mapeada pelo mapper.
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + id));
        return ResponseEntity.ok(pacienteMapper.toResponse(paciente));
    }

    @GetMapping("/email/{email}")
    // Centralizamos a busca por e-mail no service para aproveitar caches/validações e retornamos
    // o DTO mapeado garantindo consistência no contrato exposto.
    public ResponseEntity<PacienteResponseDTO> getPacienteByEmail(@PathVariable String email) {
        Paciente paciente = pacienteService.findByEmail(email);
        return ResponseEntity.ok(pacienteMapper.toResponse(paciente));
    }

    @GetMapping("/search")
    // Delegamos o filtro de nome ao service, que conhece o repositório, e convertimos o resultado
    // para DTOs antes de retornar ao cliente.
    public ResponseEntity<List<PacienteResponseDTO>> searchPacientesByNome(@RequestParam String nome) {
        List<PacienteResponseDTO> pacientes = pacienteService.findByNomeContaining(nome)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    // Transformamos o DTO em entidade via mapper e delegamos a persistência ao service para manter
    // as validações de negócio centralizadas, retornando 201 com o recurso criado.
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO request) {
        Paciente paciente = pacienteMapper.toEntity(request);
        Paciente savedPaciente = pacienteService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteMapper.toResponse(savedPaciente));
    }

    @PutMapping("/{id}")
    // Convertendo o DTO em entidade e setando o ID aqui garantimos que o service saiba qual registro
    // atualizar, mantendo toda a regra de validação e persistência centralizada nele.
    public ResponseEntity<PacienteResponseDTO> updatePaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequestDTO request) {

        Paciente pacienteAtualizado = pacienteMapper.toEntity(request);
        pacienteAtualizado.setId(id);

        Paciente updatedPaciente = pacienteService.update(id, pacienteAtualizado);
        return ResponseEntity.ok(pacienteMapper.toResponse(updatedPaciente));
    }

    @PatchMapping("/{id}")
    // Buscamos o paciente existente para garantir que atualizações parciais só ocorram sobre dados
    // válidos e aplicamos as alterações via mapper para reaproveitar a lógica de mapeamento.
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
    // Delegamos a exclusão ao service para aplicar verificações necessárias e retornamos 204 para
    // indicar que a operação foi concluída sem conteúdo adicional.
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}