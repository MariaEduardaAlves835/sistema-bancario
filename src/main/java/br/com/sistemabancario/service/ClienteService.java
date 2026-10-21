package br.com.sistemabancario.service;

import br.com.sistemabancario.exception.ClienteJaExisteException;
import br.com.sistemabancario.exception.ClienteNaoEncontradoException;
import br.com.sistemabancario.exception.CpfInvalidoException;
import br.com.sistemabancario.exception.OperacaoInvalidaException;
import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.repository.ClienteRepository;
import br.com.sistemabancario.util.Validador;
import java.time.LocalDate;
import java.util.List;

public class ClienteService {

    private final ClienteRepository repositorio;

    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Cliente cadastrar(String nome, String cpf, LocalDate dataNascimento)
            throws CpfInvalidoException, ClienteJaExisteException, OperacaoInvalidaException {
        validarDados(nome, dataNascimento);
        String cpfNormalizado = normalizarCpf(cpf);

        if (repositorio.existePorCpf(cpfNormalizado)) {
            throw new ClienteJaExisteException(cpfNormalizado);
        }

        Cliente cliente = new Cliente(nome.trim(), cpfNormalizado, dataNascimento);
        repositorio.salvar(cliente);
        return cliente;
    }

    public Cliente buscarPorCpf(String cpf) throws ClienteNaoEncontradoException {
        String cpfNormalizado = Validador.somenteDigitos(cpf);
        return repositorio.buscarPorCpf(cpfNormalizado)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpfNormalizado));
    }

    public List<Cliente> listarTodos() {
        return repositorio.listarTodos();
    }

    private void validarDados(String nome, LocalDate dataNascimento)
            throws OperacaoInvalidaException {
        if (nome == null || nome.isBlank()) {
            throw new OperacaoInvalidaException("O nome do cliente é obrigatório.");
        }
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {
            throw new OperacaoInvalidaException("Data de nascimento inválida.");
        }
    }

    private String normalizarCpf(String cpf) throws CpfInvalidoException {
        if (!Validador.cpfValido(cpf)) {
            throw new CpfInvalidoException(cpf);
        }
        return Validador.somenteDigitos(cpf);
    }
}