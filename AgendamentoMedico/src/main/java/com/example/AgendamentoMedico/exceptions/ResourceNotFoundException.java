package com.example.AgendamentoMedico.exceptions;

/**
 * Lançada quando um recurso (Paciente, Médico, Agenda, etc.) não é encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}