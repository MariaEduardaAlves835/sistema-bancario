package br.com.sistemabancario.exception;

public class ClienteJaExisteException extends NegocioException {

    public ClienteJaExisteException(String cpf) {
        super("Já existe um cliente cadastrado com o CPF: " + cpf);
    }
}