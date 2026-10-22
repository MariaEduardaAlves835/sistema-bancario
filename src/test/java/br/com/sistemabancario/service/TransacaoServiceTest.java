package br.com.sistemabancario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sistemabancario.exception.ContaNaoEncontradaException;
import br.com.sistemabancario.exception.OperacaoInvalidaException;
import br.com.sistemabancario.exception.SaldoInsuficienteException;
import br.com.sistemabancario.exception.ValorInvalidoException;
import br.com.sistemabancario.model.Banco;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.TipoTransacao;
import br.com.sistemabancario.model.Transacao;
import br.com.sistemabancario.repository.ClienteRepositoryMemoria;
import br.com.sistemabancario.repository.ContaRepositoryMemoria;
import br.com.sistemabancario.util.GeradorNumeroConta;
import br.com.sistemabancario.util.GeradorNumeroTransacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransacaoServiceTest {

    private static final String CPF = "52998224725";

    private TransacaoService service;
    private Conta poupanca;
    private Conta corrente;

    @BeforeEach
    void preparar() throws Exception {
        ClienteService clienteService = new ClienteService(new ClienteRepositoryMemoria());
        clienteService.cadastrar("Ana Souza", CPF, LocalDate.of(2000, 5, 10));
        ContaService contaService = new ContaService(new ContaRepositoryMemoria(),
                clienteService, new Banco("Banco Teste"), new GeradorNumeroConta());
        poupanca = contaService.criarContaPoupanca(CPF);
        corrente = contaService.criarContaCorrente(CPF);
        service = new TransacaoService(contaService, new GeradorNumeroTransacao());
    }

    @Test
    void depositaEConsultaSaldo() throws Exception {
        service.depositar("000001", new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), service.consultarSaldo("000001"));
    }

    @Test
    void saqueMaiorQueSaldoLancaExcecao() {
        assertThrows(SaldoInsuficienteException.class,
                () -> service.sacar("000001", new BigDecimal("10.00")));
    }

    @Test
    void valorNegativoLancaExcecao() {
        assertThrows(ValorInvalidoException.class,
                () -> service.depositar("000001", new BigDecimal("-5")));
    }

    @Test
    void transfereEntreContasERegistraNosDoisExtratos() throws Exception {
        service.depositar("000001", new BigDecimal("200.00"));

        service.transferir("000001", "000002", new BigDecimal("80.00"));

        assertEquals(new BigDecimal("120.00"), poupanca.getSaldo());
        assertEquals(new BigDecimal("80.00"), corrente.getSaldo());
        assertEquals(2, poupanca.getExtrato().size());
        assertEquals(1, corrente.getExtrato().size());
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIDA,
                corrente.getExtrato().get(0).getTipo());
        assertNotEquals(poupanca.getExtrato().get(1).getId(),
                corrente.getExtrato().get(0).getId());
    }

    @Test
    void transferenciaParaContaInexistenteNaoAlteraAOrigem() throws Exception {
        service.depositar("000001", new BigDecimal("100.00"));

        assertThrows(ContaNaoEncontradaException.class,
                () -> service.transferir("000001", "999999", new BigDecimal("50.00")));

        assertEquals(new BigDecimal("100.00"), poupanca.getSaldo());
        assertEquals(1, poupanca.getExtrato().size());
    }

    @Test
    void naoPermiteTransferirParaAMesmaConta() {
        assertThrows(OperacaoInvalidaException.class,
                () -> service.transferir("000001", "000001", new BigDecimal("10.00")));
    }

    @Test
    void extratoMantemAOrdemDasMovimentacoes() throws Exception {
        service.depositar("000001", new BigDecimal("100.00"));
        service.sacar("000001", new BigDecimal("30.00"));

        List<Transacao> extrato = service.extrato("000001");

        assertEquals(TipoTransacao.DEPOSITO, extrato.get(0).getTipo());
        assertEquals(TipoTransacao.SAQUE, extrato.get(1).getTipo());
    }
}