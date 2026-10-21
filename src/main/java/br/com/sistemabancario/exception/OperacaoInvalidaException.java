package br.com.sistemabancario.exception;

public class OperacaoInvalidaException extends NegocioException {

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}