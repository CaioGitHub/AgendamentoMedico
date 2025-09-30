package com.example.AgendamentoMedico.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadeDTO {

    private Long id;
    private String nome;

    // construtores
    public EspecialidadeDTO() {}

    public EspecialidadeDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Builder manual
    public static class Builder {
        private Long id;
        private String nome;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public EspecialidadeDTO build() {
            return new EspecialidadeDTO(id, nome);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}