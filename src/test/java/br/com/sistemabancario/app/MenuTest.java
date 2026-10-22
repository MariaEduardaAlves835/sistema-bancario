package br.com.sistemabancario.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.sistemabancario.model.Banco;
import br.com.sistemabancario.repository.ClienteRepositoryMemoria;
import br.com.sistemabancario.repository.ContaRepositoryMemoria;
import br.com.sistemabancario.service.ClienteService;
import br.com.sistemabancario.service.ContaService;
import br.com.sistemabancario.service.TransacaoService;
import br.com.sistemabancario.util.GeradorNumeroConta;
import br.com.sistemabancario.util.GeradorNumeroTransacao;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuTest {

    private static final String CRIAR_CLIENTE_E_POUPANCA = String.join("\n",
            "1", "Ana Souza", "529.982.247-25", "10/05/2000",
            "2", "529.982.247-25", "2") + "\n";

    private final PrintStream saidaOriginal = System.out;
    private final ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();

    @BeforeEach
    void capturarSaida() {
        System.setOut(new PrintStream(saidaCapturada, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    private String executar(String entradaSimulada) {
        ClienteService clienteService = new ClienteService(new ClienteRepositoryMemoria());
        ContaService contaService = new ContaService(new ContaRepositoryMemoria(),
                clienteService, new Banco("Banco Teste"), new GeradorNumeroConta());
        TransacaoService transacaoService =
                new TransacaoService(contaService, new GeradorNumeroTransacao());
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        new Menu(scanner, clienteService, contaService, transacaoService).iniciar();

        return saidaCapturada.toString(StandardCharsets.UTF_8);
    }

    @Test
    void fluxoCompletoDeCadastroContaEDeposito() {
        String saida = executar(CRIAR_CLIENTE_E_POUPANCA
                + String.join("\n", "3", "000001", "100", "6", "000001", "0") + "\n");

        assertTrue(saida.contains("Cliente cadastrado"));
        assertTrue(saida.contains("Conta criada"));
        assertTrue(saida.contains("Depósito realizado"));
        assertTrue(saida.contains("Até logo"));
    }

    @Test
    void opcaoInvalidaNaoEncerraOMenu() {
        String saida = executar("abc\n0\n");

        assertTrue(saida.contains("Opção inválida"));
        assertTrue(saida.contains("Até logo"));
    }

    @Test
    void saqueSemSaldoMostraMensagemDeErro() {
        String saida = executar(CRIAR_CLIENTE_E_POUPANCA
                + String.join("\n", "4", "000001", "50", "0") + "\n");

        assertTrue(saida.contains("Erro: Saldo insuficiente"));
    }

    @Test
    void entradaEncerradaNaoQuebraOMenu() {
        String saida = executar("");

        assertTrue(saida.contains("Entrada encerrada"));
    }
}