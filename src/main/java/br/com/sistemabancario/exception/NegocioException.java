package br.com.sistemabancario.exception;

/**
 * Classe base para todas as exceções de regra de negócio do sistema bancário.
 * É uma checked exception: obriga o tratamento explícito em operações financeiras.
 */
public abstract class NegocioException extends Exception {

    protected NegocioException(String mensagem) {
        super(mensagem);
    }
}