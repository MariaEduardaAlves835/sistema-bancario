package br.com.sistemabancario.exception;

public class ClienteNaoEncontradoException extends NegocioException {

    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente não encontrado com o CPF: " + cpf);
    }
}