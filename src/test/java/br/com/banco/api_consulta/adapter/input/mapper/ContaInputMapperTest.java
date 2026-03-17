package br.com.banco.api_consulta.adapter.input.mapper;

import br.com.banco.api_consulta.core.domain.model.Conta;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ContaInputMapperTest {

    private final ContaInputMapper mapper = new ContaInputMapper();

    @Test
    void deveMapearContaParaResponse() {
        var atualizadoEm = LocalDateTime.of(2023, 8, 13, 21, 11, 9);
        var conta = new Conta("1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);

        var response = mapper.toResponse(conta);

        assertThat(response.numConta()).isEqualTo("1369-8");
        assertThat(response.nomeCliente()).isEqualTo("Eduardo Campos Lima");
        assertThat(response.status()).isEqualTo("ATIVA");
        assertThat(response.saldo()).isEqualTo(385840.54);
        assertThat(response.atualizadoEm()).isEqualTo(atualizadoEm);
    }
}