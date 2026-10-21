package br.com.sistemabancario.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.model.Conta;
import br.com.sistemabancario.model.ContaCorrente;
import br.com.sistemabancario.model.ContaPoupanca;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaRepositoryMemoriaTest {

    private ContaRepositoryMemoria repositorio;
    private Cliente titular;

    @BeforeEach
    void preparar() {
        repositorio = new ContaRepositoryMemoria();
        titular = new Cliente("Ana Souza", "52998224725", LocalDate.of(2000, 5, 10));
    }

    @Test
    void salvaEBuscaPorNumero() {
        Conta conta = new ContaPoupanca("000001", "0001", titular);
        repositorio.salvar(conta);

        assertSame(conta, repositorio.buscarPorNumero("000001").orElseThrow());
    }

    @Test
    void numeroInexistenteRetornaVazio() {
        assertTrue(repositorio.buscarPorNumero("999999").isEmpty());
    }

    @Test
    void listaTodasAsContasSalvas() {
        repositorio.salvar(new ContaPoupanca("000001", "0001", titular));
        repositorio.salvar(new ContaCorrente("000002", "0001", titular));

        assertEquals(2, repositorio.listarTodas().size());
    }
}