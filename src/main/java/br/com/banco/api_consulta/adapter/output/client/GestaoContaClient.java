package br.com.banco.api_consulta.adapter.output.client;

import br.com.banco.api_consulta.adapter.output.mapper.ContaOutputMapper;
import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.port.output.ConsultarSaldoContaOutputPort;
import org.springframework.stereotype.Component;

@Component
public class GestaoContaClient implements ConsultarSaldoContaOutputPort {

    private final GestaoContaFeignClient feignClient;
    private final ContaOutputMapper mapper;

    public GestaoContaClient(GestaoContaFeignClient feignClient, ContaOutputMapper mapper) {
        this.feignClient = feignClient;
        this.mapper = mapper;
    }

    @Override
    public Conta buscarConta(String numeroConta) {
        return mapper.toDomain(feignClient.consultarConta(numeroConta));
    }
}