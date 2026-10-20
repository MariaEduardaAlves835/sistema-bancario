package br.com.sistemabancario.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends NegocioException {

    public SaldoInsuficienteException(String numeroConta, BigDecimal saldoDisponivel, BigDecimal valorSolicitado) {
        super(String.format(
                "Saldo insuficiente na conta %s. Saldo disponível: R$ %.2f | Valor solicitado: R$ %.2f",
                numeroConta, saldoDisponivel, valorSolicitado));
    }
}