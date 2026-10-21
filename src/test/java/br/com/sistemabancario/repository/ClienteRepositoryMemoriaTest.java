package br.com.sistemabancario.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.sistemabancario.model.Cliente;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClienteRepositoryMemoriaTest {

    private ClienteRepositoryMemoria repositorio;

    @BeforeEach
    void preparar() {
        repositorio = new ClienteRepositoryMemoria();
    }

    @Test
    void salvaEBuscaPorCpf() {
        Cliente ana = new Cliente("Ana Souza", "52998224725", LocalDate.of(2000, 5, 10));
        repositorio.salvar(ana);

        assertSame(ana, repositorio.buscarPorCpf("52998224725").orElseThrow());
        assertTrue(repositorio.existePorCpf("52998224725"));
    }

    @Test
    void cpfInexistenteRetornaVazio() {
        assertTrue(repositorio.buscarPorCpf("00000000000").isEmpty());
        assertFalse(repositorio.existePorCpf("00000000000"));
    }

    @Test
    void listaClientesNaOrdemDeCadastro() {
        Cliente ana = new Cliente("Ana Souza", "52998224725", LocalDate.of(2000, 5, 10));
        Cliente bruno = new Cliente("Bruno Lima", "11144477735", LocalDate.of(1998, 3, 2));
        repositorio.salvar(ana);
        repositorio.salvar(bruno);

        assertEquals(2, repositorio.listarTodos().size());
        assertSame(ana, repositorio.listarTodos().get(0));
        assertSame(bruno, repositorio.listarTodos().get(1));
    }
}