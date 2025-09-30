package com.example.AgendamentoMedico.services;

import com.example.AgendamentoMedico.dtos.ConvenioDTO;
import com.example.AgendamentoMedico.mappers.ConvenioMapper;
import com.example.AgendamentoMedico.models.Convenio;
import com.example.AgendamentoMedico.repositories.ConvenioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ConvenioService {

    private final ConvenioRepository repository;
    private final ConvenioMapper mapper;

    public List<ConvenioDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ConvenioDTO> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    public ConvenioDTO salvar(ConvenioDTO dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Convênio já cadastrado: " + dto.getNome());
        }
        Convenio entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public ConvenioDTO atualizar(Long id, ConvenioDTO dto) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setNome(dto.getNome());
                    entity.setCobertura(dto.getCobertura());
                    entity.setTelefoneContato(dto.getTelefoneContato());
                    return mapper.toDTO(repository.save(entity));
                })
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com id " + id));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Convênio não encontrado com id " + id);
        }
        repository.deleteById(id);
    }
}