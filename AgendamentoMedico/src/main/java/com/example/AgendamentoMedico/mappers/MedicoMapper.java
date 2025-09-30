package com.example.AgendamentoMedico.mappers;

import com.example.AgendamentoMedico.dtos.MedicoDTO;
import com.example.AgendamentoMedico.models.Especialidade;
import com.example.AgendamentoMedico.models.Medico;

import java.util.List;
import java.util.stream.Collectors;

public class MedicoMapper {

    public static MedicoDTO toDTO(Medico medico) {
        return new MedicoDTO(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getEndereco(),
                medico.getEspecialidades()
                        .stream()
                        .map(Especialidade::getNome)
                        .collect(Collectors.toList())
        );
    }

    public static Medico toEntity(MedicoDTO dto, List<Especialidade> especialidades) {
        return Medico.builder()
                .nome(dto.getNome())
                .crm(dto.getCrm())
                .endereco(dto.getEndereco())
                .especialidades(especialidades)
                .build();
    }
}
