package com.example.AgendamentoMedico.repositories;

import com.example.AgendamentoMedico.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByEmail(String email);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.convenio WHERE p.id = :id")
    Optional<Paciente> findByIdWithConvenio(@Param("id") Long id);

    boolean existsByEmail(String email);

    @Query("SELECT p FROM Paciente p WHERE p.nome LIKE %:nome%")
    List<Paciente> findByNomeContaining(@Param("nome") String nome);
}