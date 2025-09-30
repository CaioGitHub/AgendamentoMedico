package com.example.AgendamentoMedico.mappers;

import com.example.AgendamentoMedico.dtos.MedicoRequestDTO;
import com.example.AgendamentoMedico.dtos.MedicoResponseDTO;
import com.example.AgendamentoMedico.models.Especialidade;
import com.example.AgendamentoMedico.models.Medico;

import java.util.List;
import java.util.stream.Collectors;

public class MedicoMapper {

    public static MedicoResponseDTO toResponseDTO(Medico medico) {
        return new MedicoResponseDTO(
                medico.getNome(),
                medico.getCrm(),
                medico.getEndereco(),
                medico.getEspecialidades()
                        .stream()
                        .map(Especialidade::getNome)
                        .collect(Collectors.toList())
        );
    }

    public static Medico toEntity(MedicoRequestDTO dto, List<Especialidade> especialidades) {
        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setCrm(dto.getCrm());
        medico.setEndereco(dto.getEndereco());
        medico.setEspecialidades(especialidades);
        return medico;
    }
}
