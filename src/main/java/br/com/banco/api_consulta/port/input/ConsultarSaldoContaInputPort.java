package br.com.banco.api_consulta.port.input;

import br.com.banco.api_consulta.core.domain.model.Conta;

public interface ConsultarSaldoContaInputPort {

    Conta consultar(String numeroConta);
}