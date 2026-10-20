package br.com.sistemabancario.exception;

import java.math.BigDecimal;

public class ValorInvalidoException extends NegocioException {

    public ValorInvalidoException(BigDecimal valor) {
        super("Valor inválido: R$ " + valor + ". O valor deve ser maior que zero.");
    }
}