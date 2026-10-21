package br.com.sistemabancario.repository;

import br.com.sistemabancario.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    void salvar(Cliente cliente);

    Optional<Cliente> buscarPorCpf(String cpf);

    boolean existePorCpf(String cpf);

    List<Cliente> listarTodos();
}