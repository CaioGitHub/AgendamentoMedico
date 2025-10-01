package com.example.AgendamentoMedico.mappers;

import com.example.AgendamentoMedico.dtos.ConvenioResponseDTO;
import com.example.AgendamentoMedico.dtos.PacienteRequestDTO;
import com.example.AgendamentoMedico.dtos.PacienteResponseDTO;
import com.example.AgendamentoMedico.models.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequestDTO request) {
        return Paciente.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .dataNascimento(request.getDataNascimento())
                .build();
    }

    public PacienteResponseDTO toResponse(Paciente paciente) {
        return PacienteResponseDTO.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .email(paciente.getEmail())
                .telefone(paciente.getTelefone())
                .dataNascimento(paciente.getDataNascimento())
                .convenio(paciente.getConvenio() != null ?
                        ConvenioResponseDTO.builder()
                                .id(paciente.getConvenio().getId())
                                .nome(paciente.getConvenio().getNome())
                                .cobertura(paciente.getConvenio().getCobertura())
                                .telefoneContato(paciente.getConvenio().getTelefoneContato())
                                .build() : null)
                .build();
    }

    public void updateEntityFromRequest(PacienteRequestDTO request, Paciente paciente) {
        if (request.getNome() != null) {
            paciente.setNome(request.getNome());
        }
        if (request.getTelefone() != null) {
            paciente.setTelefone(request.getTelefone());
        }
        if (request.getDataNascimento() != null) {
            paciente.setDataNascimento(request.getDataNascimento());
        }
    }
}