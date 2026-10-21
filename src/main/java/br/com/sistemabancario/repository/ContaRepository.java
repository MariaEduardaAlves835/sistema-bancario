package br.com.sistemabancario.repository;

import br.com.sistemabancario.model.Conta;
import java.util.List;
import java.util.Optional;

public interface ContaRepository {

    void salvar(Conta conta);

    Optional<Conta> buscarPorNumero(String numero);

    List<Conta> listarTodas();
}