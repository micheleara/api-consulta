package br.com.banco.api_consulta.adapter.input.controller.dto.response;

import java.time.LocalDateTime;

public record ContaSaldoResponse(
        String numConta,
        String nomeCliente,
        String status,
        double saldo,
        LocalDateTime atualizadoEm
) {}