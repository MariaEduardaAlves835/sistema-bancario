package br.com.sistemabancario.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TransacaoTest {

    @Test
    void depositoNaoPossuiContaDestino() {
        Transacao transacao = new Transacao("id-1", TipoTransacao.DEPOSITO,
                new BigDecimal("100.00"), "000001");

        assertTrue(transacao.getContaDestino().isEmpty());
    }

    @Test
    void transferenciaGuardaContaDestino() {
        Transacao transacao = new Transacao("id-2", TipoTransacao.TRANSFERENCIA_ENVIADA,
                new BigDecimal("50.00"), "000001", "000002");

        assertEquals("000002", transacao.getContaDestino().orElseThrow());
    }

    @Test
    void registraValorETipoInformados() {
        Transacao transacao = new Transacao("id-3", TipoTransacao.SAQUE,
                new BigDecimal("30.00"), "000001");

        assertEquals(TipoTransacao.SAQUE, transacao.getTipo());
        assertEquals(new BigDecimal("30.00"), transacao.getValor());
    }
}