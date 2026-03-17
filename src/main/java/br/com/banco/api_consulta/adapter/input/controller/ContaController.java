package br.com.banco.api_consulta.adapter.input.controller;

import br.com.banco.api_consulta.adapter.input.controller.dto.response.ContaSaldoResponse;
import br.com.banco.api_consulta.adapter.input.controller.dto.response.ErroResponse;
import br.com.banco.api_consulta.adapter.input.mapper.ContaInputMapper;
import br.com.banco.api_consulta.port.input.ConsultarSaldoContaInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contas")
@Tag(name = "Contas", description = "Operações relacionadas à consulta de contas bancárias")
public class ContaController {

    private final ConsultarSaldoContaInputPort inputPort;
    private final ContaInputMapper mapper;

    public ContaController(ConsultarSaldoContaInputPort inputPort, ContaInputMapper mapper) {
        this.inputPort = inputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{numeroConta}/saldo")
    @Operation(
            summary = "Consultar saldo da conta",
            description = "Retorna os dados e o saldo de uma conta bancária a partir do número da conta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo consultado com sucesso",
                    content = @Content(schema = @Schema(implementation = ContaSaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço externo indisponível (circuit breaker aberto)",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))
            )
    })
    public ResponseEntity<ContaSaldoResponse> consultarSaldo(
            @Parameter(description = "Número da conta bancária", example = "1369-8")
            @PathVariable String numeroConta) {
        var conta = inputPort.consultar(numeroConta);
        return ResponseEntity.ok(mapper.toResponse(conta));
    }
}