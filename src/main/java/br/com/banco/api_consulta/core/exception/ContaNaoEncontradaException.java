package br.com.banco.api_consulta.core.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(String numeroConta) {
        super("Conta não encontrada: " + numeroConta);
    }
}