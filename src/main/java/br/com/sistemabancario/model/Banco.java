package br.com.sistemabancario.model;

public class Banco {

    private static final String AGENCIA_PADRAO = "0001";

    private final String nome;
    private final String agencia;

    public Banco(String nome) {
        this(nome, AGENCIA_PADRAO);
    }

    public Banco(String nome, String agencia) {
        this.nome = nome;
        this.agencia = agencia;
    }

    public String getNome() {
        return nome;
    }

    public String getAgencia() {
        return agencia;
    }

    @Override
    public String toString() {
        return String.format("%s | Agência %s", nome, agencia);
    }
}