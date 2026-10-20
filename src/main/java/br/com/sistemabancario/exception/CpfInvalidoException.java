package br.com.sistemabancario.exception;

public class CpfInvalidoException extends NegocioException {

    public CpfInvalidoException(String cpf) {
        super("CPF inválido: " + cpf);
    }
}