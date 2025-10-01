package com.example.AgendamentoMedico.services;

import com.example.AgendamentoMedico.models.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteService {
    List<Paciente> findAll();
    Optional<Paciente> findById(Long id);
    Paciente findByEmail(String email);
    Paciente save(Paciente paciente);
    Paciente update(Long id, Paciente paciente);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    List<Paciente> findByNomeContaining(String nome);
}