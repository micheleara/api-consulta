package br.com.banco.api_consulta.adapter.output.client;

import br.com.banco.api_consulta.adapter.output.mapper.ContaOutputMapper;
import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import br.com.banco.api_consulta.core.exception.ServicoIndisponivelException;
import br.com.banco.api_consulta.port.output.ConsultarSaldoContaOutputPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class GestaoContaClient implements ConsultarSaldoContaOutputPort {

    private static final String SERVICO = "gestao-conta";

    private final GestaoContaFeignClient feignClient;
    private final ContaOutputMapper mapper;

    public GestaoContaClient(GestaoContaFeignClient feignClient, ContaOutputMapper mapper) {
        this.feignClient = feignClient;
        this.mapper = mapper;
    }

    @Override
    @CircuitBreaker(name = SERVICO, fallbackMethod = "buscarContaFallback")
    @Retry(name = SERVICO)
    public Conta buscarConta(String numeroConta) {
        return mapper.toDomain(feignClient.consultarConta(numeroConta));
    }

    private Conta buscarContaFallback(String numeroConta, ContaNaoEncontradaException ex) {
        throw ex;
    }

    private Conta buscarContaFallback(String numeroConta, Exception ex) {
        throw new ServicoIndisponivelException(SERVICO);
    }
}