package br.com.sistemabancario.service;

import br.com.sistemabancario.exception.ContaNaoEncontradaException;
import br.com.sistemabancario.exception.OperacaoInvalidaException;
import br.com.sistemabancario.exception.SaldoInsuficienteException;
import br.com.sistemabancario.exception.ValorInvalidoException;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.Transacao;
import br.com.sistemabancario.util.GeradorNumeroTransacao;
import java.math.BigDecimal;
import java.util.List;

public class TransacaoService {

    private final ContaService contaService;
    private final GeradorNumeroTransacao geradorId;

    public TransacaoService(ContaService contaService, GeradorNumeroTransacao geradorId) {
        this.contaService = contaService;
        this.geradorId = geradorId;
    }

    public void depositar(String numeroConta, BigDecimal valor)
            throws ContaNaoEncontradaException, ValorInvalidoException {
        Conta conta = contaService.buscarPorNumero(numeroConta);
        conta.depositar(valor, geradorId.proximo());
        contaService.salvar(conta);
    }

    public void sacar(String numeroConta, BigDecimal valor)
            throws ContaNaoEncontradaException, ValorInvalidoException,
            SaldoInsuficienteException {
        Conta conta = contaService.buscarPorNumero(numeroConta);
        conta.sacar(valor, geradorId.proximo());
        contaService.salvar(conta);
    }

    public void transferir(String numeroOrigem, String numeroDestino, BigDecimal valor)
            throws ContaNaoEncontradaException, OperacaoInvalidaException,
            ValorInvalidoException, SaldoInsuficienteException {
        if (numeroOrigem.equals(numeroDestino)) {
            throw new OperacaoInvalidaException("Não é possível transferir para a mesma conta.");
        }
        Conta origem = contaService.buscarPorNumero(numeroOrigem);
        Conta destino = contaService.buscarPorNumero(numeroDestino);

        origem.enviarTransferencia(destino, valor, geradorId.proximo());
        destino.receberTransferencia(origem, valor, geradorId.proximo());

        contaService.salvar(origem);
        contaService.salvar(destino);
    }

    public BigDecimal consultarSaldo(String numeroConta) throws ContaNaoEncontradaException {
        return contaService.buscarPorNumero(numeroConta).getSaldo();
    }

    public List<Transacao> extrato(String numeroConta) throws ContaNaoEncontradaException {
        return contaService.buscarPorNumero(numeroConta).getExtrato();
    }
}