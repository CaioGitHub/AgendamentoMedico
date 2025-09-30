package com.example.AgendamentoMedico.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MedicoResponseDTO {
    private String nome;
    private String crm;
    private String endereco;
    private List<String> especialidades;

    public MedicoResponseDTO(String nome, String crm, String endereco, List<String> especialidades) {
        this.nome = nome;
        this.crm = crm;
        this.endereco = endereco;
        this.especialidades = especialidades;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
}