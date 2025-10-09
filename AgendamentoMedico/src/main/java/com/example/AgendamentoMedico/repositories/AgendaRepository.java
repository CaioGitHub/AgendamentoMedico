package com.example.AgendamentoMedico.repositories;

import com.example.AgendamentoMedico.enums.StatusAgenda;
import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.models.Medico;
import com.example.AgendamentoMedico.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    boolean existsByMedicoAndDataHoraAndStatus(Medico medico, LocalDateTime dataHora, StatusAgenda status);
    boolean existsByPacienteAndDataHoraAndStatus(Paciente paciente, LocalDateTime dataHora, StatusAgenda status);
}
