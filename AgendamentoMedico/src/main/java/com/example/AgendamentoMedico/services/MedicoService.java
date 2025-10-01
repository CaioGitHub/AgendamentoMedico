package com.example.AgendamentoMedico.services;

import com.example.AgendamentoMedico.dtos.MedicoRequestDTO;
import com.example.AgendamentoMedico.dtos.MedicoResponseDTO;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.mappers.MedicoMapper;
import com.example.AgendamentoMedico.models.Especialidade;
import com.example.AgendamentoMedico.models.Medico;
import com.example.AgendamentoMedico.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeService especialidadeService;

    public Page<MedicoResponseDTO> listarPaginado(int page, int size) {
        return medicoRepository.findAll(PageRequest.of(page, size))
                .map(MedicoMapper::toResponseDTO);
    }

    public MedicoResponseDTO buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com ID: " + id));
    }

    public Page<MedicoResponseDTO>  buscarPorEspecialidade(String especialidade, int page, int size) {
        return medicoRepository.findByEspecialidades_NomeIgnoreCase(especialidade, PageRequest.of(page, size))
                .map(MedicoMapper::toResponseDTO);
    }

    public MedicoResponseDTO salvar(MedicoRequestDTO medicoRequestDTO) {
        List<Especialidade> especialidades = especialidadeService.buscarPorIds(medicoRequestDTO.getEspecialidades());
        Medico medico = MedicoMapper.toEntity(medicoRequestDTO, especialidades);
        return MedicoMapper.toResponseDTO(medicoRepository.save(medico));
    }

    public MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com ID: " + id));

        List<Especialidade> especialidades = especialidadeService.buscarPorIds(dto.getEspecialidades());

        medico.setNome(dto.getNome());
        medico.setCrm(dto.getCrm());
        medico.setEndereco(dto.getEndereco());
        medico.setEspecialidades(especialidades);

        return MedicoMapper.toResponseDTO(medicoRepository.save(medico));
    }

    public void excluir(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com ID: " + id));
        medicoRepository.delete(medico);
    }
}
