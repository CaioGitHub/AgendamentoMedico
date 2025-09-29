package com.example.AgendamentoMedico.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false, unique = true)
    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;
}