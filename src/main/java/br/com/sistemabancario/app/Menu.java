package br.com.sistemabancario.app;

import br.com.sistemabancario.exception.NegocioException;
import br.com.sistemabancario.exception.OperacaoInvalidaException;
import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.Transacao;
import br.com.sistemabancario.service.ClienteService;
import br.com.sistemabancario.service.ContaService;
import br.com.sistemabancario.service.TransacaoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {

    @FunctionalInterface
    private interface Acao {
        void executar() throws NegocioException;
    }

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final String OPCOES = """

            ===== SISTEMA BANCÁRIO =====
            1 - Criar cliente
            2 - Criar conta
            3 - Depositar
            4 - Sacar
            5 - Transferir
            6 - Consultar saldo e dados da conta
            7 - Extrato
            8 - Listar clientes
            9 - Listar contas
            0 - Sair
            """;

    private final Scanner entrada;
    private final ClienteService clienteService;
    private final ContaService contaService;
    private final TransacaoService transacaoService;

    public Menu(Scanner entrada, ClienteService clienteService,
                ContaService contaService, TransacaoService transacaoService) {
        this.entrada = entrada;
        this.clienteService = clienteService;
        this.contaService = contaService;
        this.transacaoService = transacaoService;
    }

    public void iniciar() {
        try {
            boolean executando = true;
            while (executando) {
                System.out.print(OPCOES);
                executando = processar(lerTexto("Escolha uma opção"));
            }
            System.out.println("Até logo!");
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada. Até logo!");
        }
    }

    private boolean processar(String opcao) {
        if ("0".equals(opcao)) {
            return false;
        }
        switch (opcao) {
            case "1" -> executarComTratamento(this::criarCliente);
            case "2" -> executarComTratamento(this::criarConta);
            case "3" -> executarComTratamento(this::depositar);
            case "4" -> executarComTratamento(this::sacar);
            case "5" -> executarComTratamento(this::transferir);
            case "6" -> executarComTratamento(this::consultarConta);
            case "7" -> executarComTratamento(this::exibirExtrato);
            case "8" -> executarComTratamento(this::listarClientes);
            case "9" -> executarComTratamento(this::listarContas);
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
        return true;
    }

    private void executarComTratamento(Acao acao) {
        try {
            acao.executar();
        } catch (NegocioException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarCliente() throws NegocioException {
        String nome = lerTexto("Nome");
        String cpf = lerTexto("CPF");
        LocalDate nascimento = lerData("Data de nascimento (dd/MM/aaaa)");

        Cliente cliente = clienteService.cadastrar(nome, cpf, nascimento);
        System.out.println("Cliente cadastrado: " + cliente);
    }

    private void criarConta() throws NegocioException {
        String cpf = lerTexto("CPF do titular");
        String tipo = lerTexto("Tipo da conta (1 - Corrente, 2 - Poupança)");

        Conta conta = switch (tipo) {
            case "1" -> contaService.criarContaCorrente(cpf);
            case "2" -> contaService.criarContaPoupanca(cpf);
            default -> throw new OperacaoInvalidaException("Tipo de conta inválido.");
        };
        System.out.println("Conta criada: " + conta);
    }

    private void depositar() throws NegocioException {
        String numero = lerTexto("Número da conta");
        BigDecimal valor = lerValor("Valor do depósito");

        transacaoService.depositar(numero, valor);
        System.out.println("Depósito realizado. " + saldoAtual(numero));
    }

    private void sacar() throws NegocioException {
        String numero = lerTexto("Número da conta");
        BigDecimal valor = lerValor("Valor do saque");

        transacaoService.sacar(numero, valor);
        System.out.println("Saque realizado. " + saldoAtual(numero));
    }

    private void transferir() throws NegocioException {
        String origem = lerTexto("Conta de origem");
        String destino = lerTexto("Conta de destino");
        BigDecimal valor = lerValor("Valor da transferência");

        transacaoService.transferir(origem, destino, valor);
        System.out.println("Transferência realizada. " + saldoAtual(origem));
    }

    private void consultarConta() throws NegocioException {
        Conta conta = contaService.buscarPorNumero(lerTexto("Número da conta"));
        System.out.println(conta);
    }

    private void exibirExtrato() throws NegocioException {
        String numero = lerTexto("Número da conta");
        Conta conta = contaService.buscarPorNumero(numero);

        System.out.println("Extrato da conta " + conta.getNumero()
                + " - " + conta.getTitular().getNome());
        imprimirLista(transacaoService.extrato(numero), "Nenhuma movimentação.");
        System.out.println(saldoAtual(numero));
    }

    private void listarClientes() {
        imprimirLista(clienteService.listarTodos(), "Nenhum cliente cadastrado.");
    }

    private void listarContas() {
        imprimirLista(contaService.listarTodas(), "Nenhuma conta cadastrada.");
    }

    private String saldoAtual(String numeroConta) throws NegocioException {
        return String.format("Saldo atual: R$ %.2f", transacaoService.consultarSaldo(numeroConta));
    }

    private void imprimirLista(List<?> itens, String mensagemVazia) {
        if (itens.isEmpty()) {
            System.out.println(mensagemVazia);
            return;
        }
        itens.forEach(System.out::println);
    }

    private String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return entrada.nextLine().trim();
    }

    private BigDecimal lerValor(String rotulo) throws OperacaoInvalidaException {
        String texto = lerTexto(rotulo).replace(',', '.');
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            throw new OperacaoInvalidaException("Valor informado não é um número válido.");
        }
    }

    private LocalDate lerData(String rotulo) throws OperacaoInvalidaException {
        try {
            return LocalDate.parse(lerTexto(rotulo), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new OperacaoInvalidaException("Data inválida. Use o formato dd/MM/aaaa.");
        }
    }
}