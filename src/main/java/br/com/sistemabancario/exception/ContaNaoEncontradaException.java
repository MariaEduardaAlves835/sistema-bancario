package br.com.sistemabancario.exception;

public class ContaNaoEncontradaException extends NegocioException {

    public ContaNaoEncontradaException(String numeroConta) {
        super("Conta não encontrada: " + numeroConta);
    }
}