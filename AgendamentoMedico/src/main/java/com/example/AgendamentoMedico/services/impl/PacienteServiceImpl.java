package com.example.AgendamentoMedico.services.impl;

import com.example.AgendamentoMedico.exceptions.BusinessException;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.models.Paciente;
import com.example.AgendamentoMedico.repositories.PacienteRepository;
import com.example.AgendamentoMedico.services.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente findByEmail(String email) {
        return pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com email: " + email));
    }

    @Override
    @Transactional
    public Paciente save(Paciente paciente) {
        if (pacienteRepository.existsByEmail(paciente.getEmail())) {
            throw new BusinessException("Já existe um paciente cadastrado com este email");
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public Paciente update(Long id, Paciente pacienteAtualizado) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + id));

        // Verifica se o email foi alterado e se já existe
        if (!pacienteExistente.getEmail().equals(pacienteAtualizado.getEmail()) &&
                pacienteRepository.existsByEmail(pacienteAtualizado.getEmail())) {
            throw new BusinessException("Já existe um paciente cadastrado com este email");
        }

        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setEmail(pacienteAtualizado.getEmail());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        pacienteExistente.setConvenio(pacienteAtualizado.getConvenio());

        return pacienteRepository.save(pacienteExistente);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado com ID: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return pacienteRepository.existsByEmail(email);
    }

    @Override
    public List<Paciente> findByNomeContaining(String nome) {
        return pacienteRepository.findByNomeContaining(nome);
    }
}