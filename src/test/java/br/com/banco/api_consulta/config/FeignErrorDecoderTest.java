package br.com.banco.api_consulta.config;

import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class FeignErrorDecoderTest {

    private final FeignErrorDecoder decoder = new FeignErrorDecoder();

    @Test
    void deveRetornarContaNaoEncontradaExceptionQuando404ComNumConta() {
        var response = buildResponse(404, "http://localhost:8081/api/v1/consulta-contas?num_conta=1369-8");

        var exception = decoder.decode("GestaoContaFeignClient#consultarConta", response);

        assertThat(exception).isInstanceOf(ContaNaoEncontradaException.class);
        assertThat(exception.getMessage()).contains("1369-8");
    }

    @Test
    void deveRetornarContaNaoEncontradaExceptionQuando404SemNumConta() {
        var response = buildResponse(404, "http://localhost:8081/api/v1/consulta-contas");

        var exception = decoder.decode("GestaoContaFeignClient#consultarConta", response);

        assertThat(exception).isInstanceOf(ContaNaoEncontradaException.class);
        assertThat(exception.getMessage()).contains("desconhecido");
    }

    @Test
    void deveRetornarExcecaoGenericaQuandoStatusDiferenteDe404() {
        var response = buildResponse(500, "http://localhost:8081/api/v1/consulta-contas?num_conta=1369-8");

        var exception = decoder.decode("GestaoContaFeignClient#consultarConta", response);

        assertThat(exception).isNotInstanceOf(ContaNaoEncontradaException.class);
    }

    private Response buildResponse(int status, String url) {
        var request = Request.create(
                Request.HttpMethod.GET,
                url,
                Collections.emptyMap(),
                null,
                null,
                null
        );
        return Response.builder()
                .status(status)
                .request(request)
                .headers(Collections.emptyMap())
                .build();
    }
}