package com.example.AgendamentoMedico.dtos;

import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.example.AgendamentoMedico.enums.StatusAgenda;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoResponseDTO {
    private Long id;
    private String pacienteNome;
    private String medicoNome;
    private LocalDateTime dataHora;
    private TipoConsulta tipoConsulta;
    private StatusAgenda status;
}
