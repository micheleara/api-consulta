package br.com.banco.api_consulta.adapter.output.mapper;

import br.com.banco.api_consulta.adapter.output.client.dto.response.GestaoContaClientResponse;
import br.com.banco.api_consulta.core.domain.model.Conta;
import org.springframework.stereotype.Component;

@Component
public class ContaOutputMapper {

    public Conta toDomain(GestaoContaClientResponse response) {
        return new Conta(
                response.numConta(),
                response.nomeCliente(),
                response.status(),
                response.saldo(),
                response.atualizadoEm()
        );
    }
}