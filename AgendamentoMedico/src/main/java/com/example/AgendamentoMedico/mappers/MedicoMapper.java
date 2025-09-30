package com.example.AgendamentoMedico.mappers;

import com.example.AgendamentoMedico.dtos.MedicoRequestDTO;
import com.example.AgendamentoMedico.dtos.MedicoResponseDTO;
import com.example.AgendamentoMedico.models.Especialidade;
import com.example.AgendamentoMedico.models.Medico;

import java.util.List;
import java.util.stream.Collectors;

public class MedicoMapper {

    public static MedicoResponseDTO toResponseDTO(Medico medico) {
        return MedicoResponseDTO.builder()
                .nome(medico.getNome())
                .crm(medico.getCrm())
                .endereco(medico.getEndereco())
                .especialidades(
                        medico.getEspecialidades()
                                .stream()
                                .map(Especialidade::getNome)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static Medico toEntity(MedicoRequestDTO dto, List<Especialidade> especialidades) {
        return Medico.builder()
                .nome(dto.getNome())
                .crm(dto.getCrm())
                .endereco(dto.getEndereco())
                .especialidades(especialidades)
                .build();
    }
}
