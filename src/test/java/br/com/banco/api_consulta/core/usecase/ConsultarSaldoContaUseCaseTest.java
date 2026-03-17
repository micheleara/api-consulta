package br.com.banco.api_consulta.core.usecase;

import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import br.com.banco.api_consulta.port.output.ConsultarSaldoContaOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarSaldoContaUseCaseTest {

    @Mock
    private ConsultarSaldoContaOutputPort outputPort;

    private ConsultarSaldoContaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarSaldoContaUseCase(outputPort);
    }

    @Test
    void deveRetornarContaQuandoEncontrada() {
        var numeroConta = "1369-8";
        var conta = new Conta("1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54,
                LocalDateTime.of(2023, 8, 13, 21, 11, 9));

        when(outputPort.buscarConta(numeroConta)).thenReturn(conta);

        var resultado = useCase.consultar(numeroConta);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNumConta()).isEqualTo("1369-8");
        assertThat(resultado.getNomeCliente()).isEqualTo("Eduardo Campos Lima");
        assertThat(resultado.getStatus()).isEqualTo("ATIVA");
        assertThat(resultado.getSaldo()).isEqualTo(385840.54);
    }

    @Test
    void devePropagaExcecaoQuandoContaNaoEncontrada() {
        var numeroConta = "9999-9";
        when(outputPort.buscarConta(numeroConta)).thenThrow(new ContaNaoEncontradaException(numeroConta));

        assertThatThrownBy(() -> useCase.consultar(numeroConta))
                .isInstanceOf(ContaNaoEncontradaException.class)
                .hasMessageContaining("9999-9");
    }
}