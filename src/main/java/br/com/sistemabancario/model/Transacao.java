package br.com.sistemabancario.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class Transacao {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String id;
    private final TipoTransacao tipo;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;
    private final String contaOrigem;
    private final String contaDestino;

    public Transacao(String id, TipoTransacao tipo, BigDecimal valor,
                     String contaOrigem, String contaDestino, LocalDateTime dataHora) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.dataHora = dataHora;
    }

    public Transacao(String id, TipoTransacao tipo, BigDecimal valor,
                     String contaOrigem, String contaDestino) {
        this(id, tipo, valor, contaOrigem, contaDestino, LocalDateTime.now());
    }

    public Transacao(String id, TipoTransacao tipo, BigDecimal valor, String contaOrigem) {
        this(id, tipo, valor, contaOrigem, null);
    }

    public String getId() {
        return id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public Optional<String> getContaDestino() {
        return Optional.ofNullable(contaDestino);
    }

    @Override
    public String toString() {
        String destino = getContaDestino().map(conta -> " -> " + conta).orElse("");
        return String.format("%s | %-22s | R$ %10.2f | conta %s%s",
                dataHora.format(FORMATO_DATA_HORA), tipo, valor, contaOrigem, destino);
    }
}