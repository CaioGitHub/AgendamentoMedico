package com.example.AgendamentoMedico.dtos;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConvenioDTO {
    private Long id;
    private String nome;
    private String cobertura;
    private String telefoneContato;
}
