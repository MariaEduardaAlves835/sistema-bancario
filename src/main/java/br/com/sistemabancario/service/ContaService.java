package br.com.sistemabancario.service;

import br.com.sistemabancario.exception.ClienteNaoEncontradoException;
import br.com.sistemabancario.exception.ContaNaoEncontradaException;
import br.com.sistemabancario.model.Banco;
import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.ContaCorrente;
import br.com.sistemabancario.model.ContaPoupanca;
import br.com.sistemabancario.repository.ContaRepository;
import br.com.sistemabancario.util.GeradorNumeroConta;
import java.util.List;

public class ContaService {

    @FunctionalInterface
    private interface FabricaDeConta {
        Conta criar(String numero, String agencia, Cliente titular);
    }

    private final ContaRepository repositorio;
    private final ClienteService clienteService;
    private final Banco banco;
    private final GeradorNumeroConta geradorNumero;

    public ContaService(ContaRepository repositorio, ClienteService clienteService,
                        Banco banco, GeradorNumeroConta geradorNumero) {
        this.repositorio = repositorio;
        this.clienteService = clienteService;
        this.banco = banco;
        this.geradorNumero = geradorNumero;
    }

    public Conta criarContaCorrente(String cpf) throws ClienteNaoEncontradoException {
        return criarConta(cpf, ContaCorrente::new);
    }

    public Conta criarContaPoupanca(String cpf) throws ClienteNaoEncontradoException {
        return criarConta(cpf, ContaPoupanca::new);
    }

    public Conta buscarPorNumero(String numero) throws ContaNaoEncontradaException {
        return repositorio.buscarPorNumero(numero)
                .orElseThrow(() -> new ContaNaoEncontradaException(numero));
    }

    public List<Conta> listarTodas() {
        return repositorio.listarTodas();
    }

    public void salvar(Conta conta) {
        repositorio.salvar(conta);
    }

    public List<Conta> listarPorCliente(String cpf) throws ClienteNaoEncontradoException {
        return clienteService.buscarPorCpf(cpf).getContas();
    }

    private Conta criarConta(String cpf, FabricaDeConta fabrica)
            throws ClienteNaoEncontradoException {
        Cliente titular = clienteService.buscarPorCpf(cpf);
        Conta conta = fabrica.criar(geradorNumero.proximo(), banco.getAgencia(), titular);
        titular.adicionarConta(conta);
        repositorio.salvar(conta);
        return conta;
    }
}