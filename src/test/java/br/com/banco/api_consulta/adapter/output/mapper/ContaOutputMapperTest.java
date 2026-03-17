package br.com.banco.api_consulta.adapter.output.mapper;

import br.com.banco.api_consulta.adapter.output.client.dto.response.GestaoContaClientResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ContaOutputMapperTest {

    private final ContaOutputMapper mapper = new ContaOutputMapper();

    @Test
    void deveMapearResponseParaDomain() {
        var atualizadoEm = LocalDateTime.of(2023, 8, 13, 21, 11, 9);
        var response = new GestaoContaClientResponse(
                "1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);

        var conta = mapper.toDomain(response);

        assertThat(conta.getNumConta()).isEqualTo("1369-8");
        assertThat(conta.getNomeCliente()).isEqualTo("Eduardo Campos Lima");
        assertThat(conta.getStatus()).isEqualTo("ATIVA");
        assertThat(conta.getSaldo()).isEqualTo(385840.54);
        assertThat(conta.getAtualizadoEm()).isEqualTo(atualizadoEm);
    }
}