package br.com.banco.api_consulta.config;

import br.com.banco.api_consulta.core.usecase.ConsultarSaldoContaUseCase;
import br.com.banco.api_consulta.port.input.ConsultarSaldoContaInputPort;
import br.com.banco.api_consulta.port.output.ConsultarSaldoContaOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ConsultarSaldoContaInputPort consultarSaldoContaInputPort(ConsultarSaldoContaOutputPort outputPort) {
        return new ConsultarSaldoContaUseCase(outputPort);
    }
}