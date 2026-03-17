package br.com.banco.api_consulta.config;

import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            String url = response.request().url();
            String numConta = extrairNumConta(url);
            return new ContaNaoEncontradaException(numConta);
        }
        return defaultDecoder.decode(methodKey, response);
    }

    private String extrairNumConta(String url) {
        String param = "num_conta=";
        int index = url.indexOf(param);
        if (index != -1) {
            return url.substring(index + param.length());
        }
        return "desconhecido";
    }
}