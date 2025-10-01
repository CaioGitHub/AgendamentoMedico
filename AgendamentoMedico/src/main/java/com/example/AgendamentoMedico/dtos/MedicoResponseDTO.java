package com.example.AgendamentoMedico.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDTO {
    private String nome;
    private String crm;
    private String endereco;
    private List<String> especialidades;
}