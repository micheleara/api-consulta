package br.com.banco.api_consulta.core.domain.model;

import java.time.LocalDateTime;

public class Conta {

    private String numConta;
    private String nomeCliente;
    private String status;
    private double saldo;
    private LocalDateTime atualizadoEm;

    public Conta() {}

    public Conta(String numConta, String nomeCliente, String status, double saldo, LocalDateTime atualizadoEm) {
        this.numConta = numConta;
        this.nomeCliente = nomeCliente;
        this.status = status;
        this.saldo = saldo;
        this.atualizadoEm = atualizadoEm;
    }

    public String getNumConta() {
        return numConta;
    }

    public void setNumConta(String numConta) {
        this.numConta = numConta;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}