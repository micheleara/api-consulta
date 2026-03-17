package br.com.banco.api_consulta.adapter.input.controller;

import br.com.banco.api_consulta.adapter.input.controller.dto.response.ErroResponse;
import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import br.com.banco.api_consulta.core.exception.ServicoIndisponivelException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    @ApiResponse(
            responseCode = "404",
            description = "Conta não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))
    )
    public ResponseEntity<ErroResponse> handleContaNaoEncontrada(ContaNaoEncontradaException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Conta não encontrada",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ServicoIndisponivelException.class)
    @ApiResponse(
            responseCode = "503",
            description = "Serviço externo indisponível",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))
    )
    public ResponseEntity<ErroResponse> handleServicoIndisponivel(ServicoIndisponivelException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErroResponse(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "Serviço indisponível",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErroResponse.class))
    )
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro interno do servidor",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}