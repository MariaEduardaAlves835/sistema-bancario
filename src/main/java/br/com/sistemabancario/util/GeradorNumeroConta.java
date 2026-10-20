package br.com.sistemabancario.util;

public class GeradorNumeroConta {

    private int ultimoNumero;

    public GeradorNumeroConta() {
        this(0);
    }

    public GeradorNumeroConta(int ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public String proximo() {
        ultimoNumero++;
        return String.format("%06d", ultimoNumero);
    }
}