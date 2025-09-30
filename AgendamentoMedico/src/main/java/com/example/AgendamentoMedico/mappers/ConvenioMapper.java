package com.example.AgendamentoMedico.mappers;

import com.example.AgendamentoMedico.dtos.ConvenioDTO;
import com.example.AgendamentoMedico.models.Convenio;
import org.springframework.stereotype.Component;

@Component
public class ConvenioMapper {

    public ConvenioDTO toDTO(Convenio entity) {
        if (entity == null) return null;
        return ConvenioDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cobertura(entity.getCobertura())
                .telefoneContato(entity.getTelefoneContato())
                .build();
    }

    public Convenio toEntity(ConvenioDTO dto) {
        if (dto == null) return null;
        return Convenio.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cobertura(dto.getCobertura())
                .telefoneContato(dto.getTelefoneContato())
                .build();
    }
}