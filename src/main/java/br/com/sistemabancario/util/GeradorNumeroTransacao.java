package br.com.sistemabancario.util;

import java.util.UUID;

public class GeradorNumeroTransacao {

    public String proximo() {
        return UUID.randomUUID().toString();
    }
}