package br.com.sistemabancario.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente extends Pessoa {

    private final List<Conta> contas = new ArrayList<>();

    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        super(nome, cpf, dataNascimento);
    }

    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }

    @Override
    public String toString() {
        return String.format("%s | CPF: %s | Contas: %d", getNome(), getCpf(), contas.size());
    }
}