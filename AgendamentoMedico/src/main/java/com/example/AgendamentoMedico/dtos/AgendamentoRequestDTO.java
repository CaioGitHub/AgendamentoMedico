package com.example.AgendamentoMedico.dtos;

import com.example.AgendamentoMedico.enums.TipoConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoRequestDTO {

    @NotNull(message = "pacienteId é obrigatório")
    private Long pacienteId;

    @NotNull(message = "medicoId é obrigatório")
    private Long medicoId;

    @NotNull(message = "dataHora é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHora;

    @NotNull(message = "tipoConsulta é obrigatório")
    private TipoConsulta tipoConsulta;
}
