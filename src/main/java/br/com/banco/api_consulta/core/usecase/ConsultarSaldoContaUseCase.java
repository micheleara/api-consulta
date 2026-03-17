package br.com.banco.api_consulta.core.usecase;

import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.port.input.ConsultarSaldoContaInputPort;
import br.com.banco.api_consulta.port.output.ConsultarSaldoContaOutputPort;

public class ConsultarSaldoContaUseCase implements ConsultarSaldoContaInputPort {

    private final ConsultarSaldoContaOutputPort outputPort;

    public ConsultarSaldoContaUseCase(ConsultarSaldoContaOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public Conta consultar(String numeroConta) {
        return outputPort.buscarConta(numeroConta);
    }
}