package com.example.AgendamentoMedico.dtos;

import lombok.AllArgsConstructor;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvenioResponseDTO {

    private Long id;

    private String nome;

    private String cobertura;

    private String telefoneContato;
}