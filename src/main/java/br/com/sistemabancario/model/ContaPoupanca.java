package br.com.sistemabancario.model;

import java.math.BigDecimal;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String numero, String agencia, Cliente titular) {
        super(numero, agencia, titular);
    }

    @Override
    public BigDecimal saldoDisponivel() {
        return getSaldo();
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}