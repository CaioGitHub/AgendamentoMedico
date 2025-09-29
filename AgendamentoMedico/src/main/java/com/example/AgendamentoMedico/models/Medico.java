package com.example.AgendamentoMedico.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false, unique=true)
    private String crm;

    @Column(nullable=false)
    private String endereco;

    @ManyToOne
    @JoinColumn(name="especialidade_id", nullable=false)
    private Especialidade especialidade;
}