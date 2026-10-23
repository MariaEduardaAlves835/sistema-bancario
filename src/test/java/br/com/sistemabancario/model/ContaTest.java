package br.com.sistemabancario.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sistemabancario.exception.SaldoInsuficienteException;
import br.com.sistemabancario.exception.ValorInvalidoException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaTest {

    private ContaPoupanca poupanca;
    private ContaCorrente corrente;

    @BeforeEach
    void preparar() {
        Cliente titular = new Cliente("Ana Souza", "52998224725", LocalDate.of(2000, 5, 10));
        poupanca = new ContaPoupanca("000001", "0001", titular);
        corrente = new ContaCorrente("000002", "0001", titular);
    }

    @Test
    void depositoAumentaSaldoERegistraTransacao() throws Exception {
        poupanca.depositar(new BigDecimal("100.00"), "t1");

        assertEquals(new BigDecimal("100.00"), poupanca.getSaldo());
        assertEquals(1, poupanca.getExtrato().size());
        assertEquals(TipoTransacao.DEPOSITO, poupanca.getExtrato().get(0).getTipo());
    }

    @Test
    void saqueMaiorQueSaldoNaPoupancaLancaExcecaoESaldoNaoMuda() throws Exception {
        poupanca.depositar(new BigDecimal("50.00"), "t1");

        assertThrows(SaldoInsuficienteException.class,
                () -> poupanca.sacar(new BigDecimal("50.01"), "t2"));
        assertEquals(new BigDecimal("50.00"), poupanca.getSaldo());
    }

    @Test
    void contaCorrentePermiteSaqueAteOLimiteDoChequeEspecial() throws Exception {
        corrente.sacar(new BigDecimal("500.00"), "t1");

        assertEquals(new BigDecimal("-500.00"), corrente.getSaldo());
        assertThrows(SaldoInsuficienteException.class,
                () -> corrente.sacar(new BigDecimal("0.01"), "t2"));
    }

    @Test
    void valorNegativoOuZeradoLancaExcecao() {
        assertThrows(ValorInvalidoException.class,
                () -> poupanca.depositar(new BigDecimal("-10"), "t1"));
        assertThrows(ValorInvalidoException.class,
                () -> poupanca.depositar(BigDecimal.ZERO, "t2"));
    }

    @Test
    void transferenciaMovimentaAsDuasContasERegistraNosDoisExtratos() throws Exception {
        poupanca.depositar(new BigDecimal("200.00"), "t1");

        poupanca.enviarTransferencia(corrente, new BigDecimal("80.00"), "t2");
        corrente.receberTransferencia(poupanca, new BigDecimal("80.00"), "t3");

        assertEquals(new BigDecimal("120.00"), poupanca.getSaldo());
        assertEquals(new BigDecimal("80.00"), corrente.getSaldo());
        assertEquals(2, poupanca.getExtrato().size());
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIDA,
                corrente.getExtrato().get(0).getTipo());
    }

    @Test
    void restauraSaldoEExtratoAPartirDoHistorico() throws Exception {
        poupanca.depositar(new BigDecimal("200.00"), "t1");
        poupanca.sacar(new BigDecimal("50.00"), "t2");
        List<Transacao> historico = new ArrayList<>(poupanca.getExtrato());

        ContaPoupanca restaurada = new ContaPoupanca("000001", "0001", poupanca.getTitular());
        restaurada.restaurarHistorico(historico);

        assertEquals(new BigDecimal("150.00"), restaurada.getSaldo());
        assertEquals(2, restaurada.getExtrato().size());
    }
}