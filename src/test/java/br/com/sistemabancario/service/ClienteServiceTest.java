package br.com.sistemabancario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sistemabancario.exception.ClienteJaExisteException;
import br.com.sistemabancario.exception.ClienteNaoEncontradoException;
import br.com.sistemabancario.exception.CpfInvalidoException;
import br.com.sistemabancario.exception.OperacaoInvalidaException;
import br.com.sistemabancario.model.Cliente;
import br.com.sistemabancario.repository.ClienteRepositoryMemoria;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClienteServiceTest {

    private static final LocalDate NASCIMENTO = LocalDate.of(2000, 5, 10);

    private ClienteService service;

    @BeforeEach
    void preparar() {
        service = new ClienteService(new ClienteRepositoryMemoria());
    }

    @Test
    void cadastraClienteGuardandoSomenteOsDigitosDoCpf() throws Exception {
        Cliente cliente = service.cadastrar("Ana Souza", "529.982.247-25", NASCIMENTO);

        assertEquals("52998224725", cliente.getCpf());
        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void rejeitaCpfInvalido() {
        assertThrows(CpfInvalidoException.class,
                () -> service.cadastrar("Ana Souza", "111.111.111-11", NASCIMENTO));
    }

    @Test
    void rejeitaCpfDuplicadoMesmoComFormatacaoDiferente() throws Exception {
        service.cadastrar("Ana Souza", "529.982.247-25", NASCIMENTO);

        assertThrows(ClienteJaExisteException.class,
                () -> service.cadastrar("Outra Pessoa", "52998224725", NASCIMENTO));
        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void rejeitaNomeEmBranco() {
        assertThrows(OperacaoInvalidaException.class,
                () -> service.cadastrar("   ", "529.982.247-25", NASCIMENTO));
    }

    @Test
    void rejeitaDataDeNascimentoNoFuturo() {
        assertThrows(OperacaoInvalidaException.class,
                () -> service.cadastrar("Ana Souza", "529.982.247-25",
                        LocalDate.now().plusDays(1)));
    }

    @Test
    void buscaPorCpfInexistenteLancaExcecao() {
        assertThrows(ClienteNaoEncontradoException.class,
                () -> service.buscarPorCpf("529.982.247-25"));
    }
}