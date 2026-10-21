package br.com.sistemabancario.repository;

import br.com.sistemabancario.model.Cliente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClienteRepositoryMemoria implements ClienteRepository {

    private final Map<String, Cliente> clientes = new LinkedHashMap<>();

    @Override
    public void salvar(Cliente cliente) {
        clientes.put(cliente.getCpf(), cliente);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return Optional.ofNullable(clientes.get(cpf));
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return clientes.containsKey(cpf);
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }
}