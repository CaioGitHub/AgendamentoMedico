package com.example.AgendamentoMedico.repositories;

import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
}
