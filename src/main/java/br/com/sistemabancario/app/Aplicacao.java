package br.com.sistemabancario.app;

import br.com.sistemabancario.model.Banco;
import br.com.sistemabancario.repository.ClienteRepositoryMemoria;
import br.com.sistemabancario.repository.ContaRepositoryMemoria;
import br.com.sistemabancario.service.ClienteService;
import br.com.sistemabancario.service.ContaService;
import br.com.sistemabancario.service.TransacaoService;
import br.com.sistemabancario.util.GeradorNumeroConta;
import br.com.sistemabancario.util.GeradorNumeroTransacao;
import java.util.Scanner;

public class Aplicacao {

    public void iniciar() {
        ClienteService clienteService = new ClienteService(new ClienteRepositoryMemoria());
        ContaService contaService = new ContaService(new ContaRepositoryMemoria(),
                clienteService, new Banco("Banco Digital"), new GeradorNumeroConta());
        TransacaoService transacaoService =
                new TransacaoService(contaService, new GeradorNumeroTransacao());

        try (Scanner scanner = new Scanner(System.in)) {
            new Menu(scanner, clienteService, contaService, transacaoService).iniciar();
        }
    }
}