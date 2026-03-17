package br.com.banco.api_consulta.adapter.input.controller.dto.response;

import java.time.LocalDateTime;

public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp
) {}