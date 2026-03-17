package br.com.banco.api_consulta.adapter.input.mapper;

import br.com.banco.api_consulta.adapter.input.controller.dto.response.ContaSaldoResponse;
import br.com.banco.api_consulta.core.domain.model.Conta;
import org.springframework.stereotype.Component;

@Component
public class ContaInputMapper {

    public ContaSaldoResponse toResponse(Conta conta) {
        return new ContaSaldoResponse(
                conta.getNumConta(),
                conta.getNomeCliente(),
                conta.getStatus(),
                conta.getSaldo(),
                conta.getAtualizadoEm()
        );
    }
}