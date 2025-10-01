package com.example.AgendamentoMedico.dtos;

import lombok.*;
import java.time.LocalDate;
import java.util.stream.DoubleStream;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private ConvenioResponseDTO convenio;
}