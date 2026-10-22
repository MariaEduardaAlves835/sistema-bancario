package br.com.sistemabancario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.sistemabancario.exception.ClienteNaoEncontradoException;
import br.com.sistemabancario.exception.ContaNaoEncontradaException;
import br.com.sistemabancario.model.Banco;
import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.ContaCorrente;
import br.com.sistemabancario.model.ContaPoupanca;
import br.com.sistemabancario.repository.ClienteRepositoryMemoria;
import br.com.sistemabancario.repository.ContaRepositoryMemoria;
import br.com.sistemabancario.util.GeradorNumeroConta;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaServiceTest {

    private static final String CPF = "529.982.247-25";

    private ContaService contaService;
    private Cliente ana;

    @BeforeEach
    void preparar() throws Exception {
        ClienteService clienteService = new ClienteService(new ClienteRepositoryMemoria());
        contaService = new ContaService(new ContaRepositoryMemoria(), clienteService,
                new Banco("Banco Teste"), new GeradorNumeroConta());
        ana = clienteService.cadastrar("Ana Souza", CPF, LocalDate.of(2000, 5, 10));
    }

    @Test
    void criaContaCorrenteComNumeroEAgenciaGerados() throws Exception {
        Conta conta = contaService.criarContaCorrente(CPF);

        assertEquals("000001", conta.getNumero());
        assertEquals("0001", conta.getAgencia());
        assertSame(ana, conta.getTitular());
        assertInstanceOf(ContaCorrente.class, conta);
    }

    @Test
    void numerosDasContasSaoSequenciais() throws Exception {
        Conta primeira = contaService.criarContaPoupanca(CPF);
        Conta segunda = contaService.criarContaCorrente(CPF);

        assertEquals("000001", primeira.getNumero());
        assertEquals("000002", segunda.getNumero());
        assertInstanceOf(ContaPoupanca.class, primeira);
        assertEquals(2, contaService.listarTodas().size());
    }

    @Test
    void titularPassaAConhecerSuasContas() throws Exception {
        contaService.criarContaPoupanca(CPF);
        contaService.criarContaCorrente(CPF);

        assertEquals(2, ana.getContas().size());
        assertEquals(2, contaService.listarPorCliente(CPF).size());
    }

    @Test
    void naoCriaContaParaClienteInexistente() {
        assertThrows(ClienteNaoEncontradoException.class,
                () -> contaService.criarContaPoupanca("111.444.777-35"));
        assertTrue(contaService.listarTodas().isEmpty());
    }

    @Test
    void buscaDeContaInexistenteLancaExcecao() {
        assertThrows(ContaNaoEncontradaException.class,
                () -> contaService.buscarPorNumero("999999"));
    }
}