package br.com.sistemabancario.repository;

import br.com.sistemabancario.model.Conta;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContaRepositoryMemoria implements ContaRepository {

    private final Map<String, Conta> contas = new LinkedHashMap<>();

    @Override
    public void salvar(Conta conta) {
        contas.put(conta.getNumero(), conta);
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        return Optional.ofNullable(contas.get(numero));
    }

    @Override
    public List<Conta> listarTodas() {
        return new ArrayList<>(contas.values());
    }
}