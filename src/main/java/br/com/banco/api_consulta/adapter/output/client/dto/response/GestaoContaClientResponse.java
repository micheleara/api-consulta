package br.com.banco.api_consulta.adapter.output.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record GestaoContaClientResponse(
        @JsonProperty("numConta") String numConta,
        @JsonProperty("nomeCliente") String nomeCliente,
        @JsonProperty("status") String status,
        @JsonProperty("saldo") double saldo,
        @JsonProperty("atualizadoEm") LocalDateTime atualizadoEm
) {}