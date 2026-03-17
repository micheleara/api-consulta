package br.com.banco.api_consulta.core.exception;

public class ServicoIndisponivelException extends RuntimeException {

    public ServicoIndisponivelException(String servico) {
        super("Serviço indisponível no momento: " + servico);
    }
}