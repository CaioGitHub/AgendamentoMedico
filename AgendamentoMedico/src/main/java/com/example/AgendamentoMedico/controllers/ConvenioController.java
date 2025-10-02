package com.example.AgendamentoMedico.controllers;

import com.example.AgendamentoMedico.dtos.ConvenioDTO;
import com.example.AgendamentoMedico.services.ConvenioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@Tag(name = "Convênios", description = "Gerenciamento de convênios médicos")
@RequestMapping("/api/convenios")
@RequiredArgsConstructor
public class ConvenioController {

    private final ConvenioService service;

    @GetMapping
    @Operation(summary = "Lista todos os convênios", description = "Retorna uma lista com todos os convênios cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    // Mantemos a listagem concentrada no service para aplicar regras de negócio e retornamos DTOs
    // para evitar expor o modelo interno diretamente aos consumidores da API.
    public ResponseEntity<List<ConvenioDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca convênio por ID", description = "Retorna os dados de um convênio específico.")
    @ApiResponse(responseCode = "200", description = "Convênio encontrado")
    @ApiResponse(responseCode = "404", description = "Convênio não encontrado")
    // Utilizamos Optional no service para encapsular a regra de existência e responder 404 quando
    // o recurso não é encontrado, alinhando o comportamento às práticas REST.
    public ResponseEntity<ConvenioDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo convênio")
    @ApiResponse(responseCode = "201", description = "Convênio criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação")
    // Encaminhamos o DTO recebido diretamente ao service para aproveitar as validações de negócio
    // centralizadas e retornamos o objeto salvo para confirmar os dados persistidos.
    public ResponseEntity<ConvenioDTO> salvar(@RequestBody ConvenioDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um convênio existente", description = "Altera os dados de um convênio pelo ID informado.")
    @ApiResponse(responseCode = "200", description = "Convênio atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação")
    @ApiResponse(responseCode = "404", description = "Convênio não encontrado")
    // A atualização delegada ao service garante reutilização de regras de validação e permite que
    // o controller continue apenas coordenando a requisição e a resposta HTTP.
    public ResponseEntity<ConvenioDTO> atualizar(@PathVariable Long id,
                                                 @RequestBody ConvenioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um convênio", description = "Exclui permanentemente um convênio pelo ID informado.")
    @ApiResponse(responseCode = "204", description = "Convênio removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Convênio não encontrado")
    // O método apenas dispara a remoção no service, que concentra verificações de integridade, e
    // retornamos 204 para indicar sucesso sem conteúdo adicional.
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
