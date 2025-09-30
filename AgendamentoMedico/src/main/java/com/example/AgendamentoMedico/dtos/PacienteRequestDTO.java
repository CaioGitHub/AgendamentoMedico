package com.example.AgendamentoMedico.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$|^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$",
            message = "Telefone inválido")
    private String telefone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataNascimento;

    private Long convenioId;
}
