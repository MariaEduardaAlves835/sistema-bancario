package br.com.sistemabancario.model;

import br.com.sistemabancario.exception.SaldoInsuficienteException;
import br.com.sistemabancario.exception.ValorInvalidoException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Conta {

    private static final int CASAS_DECIMAIS = 2;

    private final String numero;
    private final String agencia;
    private final Cliente titular;
    private final List<Transacao> historico = new ArrayList<>();
    private BigDecimal saldo = BigDecimal.ZERO.setScale(CASAS_DECIMAIS);

    protected Conta(String numero, String agencia, Cliente titular) {
        this.numero = numero;
        this.agencia = agencia;
        this.titular = titular;
    }

    public abstract BigDecimal saldoDisponivel();

    public abstract String getTipoConta();

    public void depositar(BigDecimal valor, String idTransacao) throws ValorInvalidoException {
        BigDecimal valorNormalizado = creditar(valor);
        registrar(new Transacao(idTransacao, TipoTransacao.DEPOSITO, valorNormalizado, numero));
    }

    public void sacar(BigDecimal valor, String idTransacao)
            throws ValorInvalidoException, SaldoInsuficienteException {
        BigDecimal valorNormalizado = debitar(valor);
        registrar(new Transacao(idTransacao, TipoTransacao.SAQUE, valorNormalizado, numero));
    }

    public void enviarTransferencia(Conta destino, BigDecimal valor, String idTransacao)
            throws ValorInvalidoException, SaldoInsuficienteException {
        BigDecimal valorNormalizado = debitar(valor);
        registrar(new Transacao(idTransacao, TipoTransacao.TRANSFERENCIA_ENVIADA,
                valorNormalizado, numero, destino.getNumero()));
    }

    public void receberTransferencia(Conta origem, BigDecimal valor, String idTransacao)
            throws ValorInvalidoException {
        BigDecimal valorNormalizado = creditar(valor);
        registrar(new Transacao(idTransacao, TipoTransacao.TRANSFERENCIA_RECEBIDA,
                valorNormalizado, origem.getNumero(), numero));
    }

    private BigDecimal creditar(BigDecimal valor) throws ValorInvalidoException {
        BigDecimal valorNormalizado = normalizar(valor);
        saldo = saldo.add(valorNormalizado);
        return valorNormalizado;
    }

    private BigDecimal debitar(BigDecimal valor)
            throws ValorInvalidoException, SaldoInsuficienteException {
        BigDecimal valorNormalizado = normalizar(valor);
        if (valorNormalizado.compareTo(saldoDisponivel()) > 0) {
            throw new SaldoInsuficienteException(numero, saldoDisponivel(), valorNormalizado);
        }
        saldo = saldo.subtract(valorNormalizado);
        return valorNormalizado;
    }

    private BigDecimal normalizar(BigDecimal valor) throws ValorInvalidoException {
        if (valor == null) {
            throw new ValorInvalidoException(null);
        }
        BigDecimal valorNormalizado = valor.setScale(CASAS_DECIMAIS, RoundingMode.HALF_EVEN);
        if (valorNormalizado.signum() <= 0) {
            throw new ValorInvalidoException(valor);
        }
        return valorNormalizado;
    }

    private void registrar(Transacao transacao) {
        historico.add(transacao);
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public List<Transacao> getExtrato() {
        return Collections.unmodifiableList(historico);
    }

    @Override
    public String toString() {
        return String.format("%s | Ag. %s | Nº %s | Titular: %s | Saldo: R$ %.2f",
                getTipoConta(), agencia, numero, titular.getNome(), saldo);
    }
}