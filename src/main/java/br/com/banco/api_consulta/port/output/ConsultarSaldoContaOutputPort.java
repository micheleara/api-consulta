package br.com.banco.api_consulta.port.output;

import br.com.banco.api_consulta.core.domain.model.Conta;

public interface ConsultarSaldoContaOutputPort {

    Conta buscarConta(String numeroConta);
}