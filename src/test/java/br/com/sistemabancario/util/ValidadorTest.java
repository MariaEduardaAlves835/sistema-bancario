package br.com.sistemabancario.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidadorTest {

    @Test
    void aceitaCpfValidoComFormatacao() {
        assertTrue(Validador.cpfValido("529.982.247-25"));
    }

    @Test
    void aceitaCpfValidoSemFormatacao() {
        assertTrue(Validador.cpfValido("52998224725"));
    }

    @Test
    void rejeitaDigitoVerificadorErrado() {
        assertFalse(Validador.cpfValido("529.982.247-24"));
    }

    @Test
    void rejeitaTodosDigitosIguais() {
        assertFalse(Validador.cpfValido("111.111.111-11"));
    }

    @Test
    void rejeitaTamanhoErrado() {
        assertFalse(Validador.cpfValido("123"));
    }

    @Test
    void rejeitaNulo() {
        assertFalse(Validador.cpfValido(null));
    }
}