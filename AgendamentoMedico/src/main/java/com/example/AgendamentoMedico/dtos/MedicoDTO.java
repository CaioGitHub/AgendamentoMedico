package com.example.AgendamentoMedico.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {
    private Long id;
    private String nome;
    private String crm;
    private String endereco;
    private List<String> especialidades;
}