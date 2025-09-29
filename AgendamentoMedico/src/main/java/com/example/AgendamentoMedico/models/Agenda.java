package com.example.AgendamentoMedico.models;

import com.example.AgendamentoMedico.enums.StatusAgenda;
import com.example.AgendamentoMedico.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="paciente_id", nullable=false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name="medico_id", nullable=false)
    private Medico medico;

    @Column(nullable=false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TipoConsulta tipoConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StatusAgenda status;
}
