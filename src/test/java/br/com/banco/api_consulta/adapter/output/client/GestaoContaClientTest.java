package br.com.banco.api_consulta.adapter.output.client;

import br.com.banco.api_consulta.adapter.output.client.dto.response.GestaoContaClientResponse;
import br.com.banco.api_consulta.adapter.output.mapper.ContaOutputMapper;
import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import br.com.banco.api_consulta.core.exception.ServicoIndisponivelException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestaoContaClientTest {

    @Mock
    private GestaoContaFeignClient feignClient;

    @Mock
    private ContaOutputMapper mapper;

    @InjectMocks
    private GestaoContaClient gestaoContaClient;

    @Test
    void deveBuscarContaComSucesso() {
        var numeroConta = "1369-8";
        var atualizadoEm = LocalDateTime.of(2023, 8, 13, 21, 11, 9);
        var feignResponse = new GestaoContaClientResponse(
                "1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);
        var conta = new Conta("1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);

        when(feignClient.consultarConta(numeroConta)).thenReturn(feignResponse);
        when(mapper.toDomain(feignResponse)).thenReturn(conta);

        var resultado = gestaoContaClient.buscarConta(numeroConta);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNumConta()).isEqualTo("1369-8");
        assertThat(resultado.getNomeCliente()).isEqualTo("Eduardo Campos Lima");
        assertThat(resultado.getSaldo()).isEqualTo(385840.54);
    }

    @Test
    void devePropagaContaNaoEncontradaExceptionSemFallback() {
        var numeroConta = "9999-9";
        when(feignClient.consultarConta(numeroConta))
                .thenThrow(new ContaNaoEncontradaException(numeroConta));

        assertThatThrownBy(() -> gestaoContaClient.buscarConta(numeroConta))
                .isInstanceOf(ContaNaoEncontradaException.class)
                .hasMessageContaining("9999-9");
    }

}