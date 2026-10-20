package br.com.sistemabancario.model;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {

    private static final BigDecimal LIMITE_PADRAO = new BigDecimal("500.00");

    private final BigDecimal limiteChequeEspecial;

    public ContaCorrente(String numero, String agencia, Cliente titular) {
        this(numero, agencia, titular, LIMITE_PADRAO);
    }

    public ContaCorrente(String numero, String agencia, Cliente titular,
                         BigDecimal limiteChequeEspecial) {
        super(numero, agencia, titular);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    public BigDecimal saldoDisponivel() {
        return getSaldo().add(limiteChequeEspecial);
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }

    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }
}