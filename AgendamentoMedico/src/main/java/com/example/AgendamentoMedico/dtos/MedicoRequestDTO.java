package com.example.AgendamentoMedico.dtos;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoRequestDTO {
    private Long id;
    private String nome;
    private String crm;
    private String endereco;
    private List<Long> especialidades;
}
