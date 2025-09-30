package com.example.AgendamentoMedico.services;

import com.example.AgendamentoMedico.dtos.MedicoDTO;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.mappers.MedicoMapper;
import com.example.AgendamentoMedico.models.Medico;
import com.example.AgendamentoMedico.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll()
                .stream()
                .map(MedicoMapper::toDTO)
                .toList();
    }

    public MedicoDTO buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com ID: " + id));
    }

    public List<MedicoDTO> buscarPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidades_NomeIgnoreCase(especialidade)
                .stream()
                .map(MedicoMapper::toDTO)
                .toList();
    }

    public void excluir(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com ID: " + id));
        medicoRepository.delete(medico);
    }
}
