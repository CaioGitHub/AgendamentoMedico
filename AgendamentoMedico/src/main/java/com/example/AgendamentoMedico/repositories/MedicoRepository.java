package com.example.AgendamentoMedico.repositories;

import com.example.AgendamentoMedico.models.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCrm(String crm);
    Page<Medico> findByEspecialidades_NomeIgnoreCase(String especialidade, Pageable pageable);
}
