package com.example.AgendamentoMedico.services;

import com.example.AgendamentoMedico.enums.StatusAgenda;
import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.example.AgendamentoMedico.exceptions.ResourceNotFoundException;
import com.example.AgendamentoMedico.models.Agenda;
import com.example.AgendamentoMedico.models.Medico;
import com.example.AgendamentoMedico.models.Paciente;
import com.example.AgendamentoMedico.repositories.AgendaRepository;
import com.example.AgendamentoMedico.repositories.MedicoRepository;
import com.example.AgendamentoMedico.repositories.PacienteRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public AgendaService(AgendaRepository agendaRepository,
                         PacienteRepository pacienteRepository,
                         MedicoRepository medicoRepository) {
        this.agendaRepository = agendaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public List<Agenda> listar() {
        return agendaRepository.findAll();
    }

    /**
     * Busca agenda por ID.
     */
    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));
    }

    /**
     * Agenda uma nova consulta com validações.
     */
    public Agenda agendar(Long pacienteId, Long medicoId, LocalDateTime dataHora,
                          TipoConsulta tipoConsulta) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        if (paciente.getConvenio() == null) {
            throw new IllegalArgumentException("Paciente não possui convênio vinculado.");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data do agendamento não pode ser anterior à data atual.");
        }

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        boolean ocupado = agendaRepository
                .existsByMedicoAndDataHoraAndStatus(medico, dataHora, StatusAgenda.AGENDADA);
        if (ocupado) {
            throw new IllegalArgumentException("O médico já possui um agendamento neste horário.");
        }

        boolean pacienteJaAgendado = agendaRepository
                .existsByPacienteAndDataHoraAndStatus(paciente, dataHora, StatusAgenda.AGENDADA);
        if (pacienteJaAgendado) {
            throw new IllegalArgumentException("O paciente já possui um agendamento neste horário.");
        }

        Agenda novaAgenda = Agenda.builder()
                .paciente(paciente)
                .medico(medico)
                .dataHora(dataHora)
                .tipoConsulta(tipoConsulta)
                .status(StatusAgenda.AGENDADA)
                .build();

        return agendaRepository.save(novaAgenda);
    }

    /**
     * Reagenda uma consulta (altera data/hora e tipo de consulta).
     */
    public Agenda remarcar(Long idAgenda, LocalDateTime novaDataHora, TipoConsulta novoTipo) {
        Agenda agenda = buscarPorId(idAgenda);

        if (novaDataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A nova data do agendamento não pode ser anterior à data atual.");
        }

        boolean ocupado = agendaRepository
                .existsByMedicoAndDataHoraAndStatus(agenda.getMedico(), novaDataHora, StatusAgenda.AGENDADA);
        if (ocupado && !agenda.getDataHora().equals(novaDataHora)) {
            throw new IllegalArgumentException("O médico já possui um agendamento neste horário.");
        }

        boolean pacienteJaAgendado = agendaRepository
                .existsByPacienteAndDataHoraAndStatus(agenda.getPaciente(), novaDataHora, StatusAgenda.AGENDADA);
        if (pacienteJaAgendado && !agenda.getDataHora().equals(novaDataHora)) {
            throw new IllegalArgumentException("O paciente já possui um agendamento neste horário.");
        }

        agenda.setDataHora(novaDataHora);
        agenda.setTipoConsulta(novoTipo);
        agenda.setStatus(StatusAgenda.AGENDADA);

        return agendaRepository.save(agenda);
    }

    /**
     * Cancela uma consulta existente.
     */
    public Agenda cancelar(Long idAgenda) {
        Agenda agenda = buscarPorId(idAgenda);
        agenda.setStatus(StatusAgenda.CANCELADA);
        return agendaRepository.save(agenda);
    }

    /**
     * Remove uma consulta do sistema.
     */
    public void deletar(Long idAgenda) {
        agendaRepository.delete(buscarPorId(idAgenda));
    }
}
