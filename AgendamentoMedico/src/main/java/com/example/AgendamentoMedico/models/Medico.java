package com.example.AgendamentoMedico.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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
    private List<Especialidade> especialidade;
}