package br.com.banco.api_consulta.adapter.input.controller;

import br.com.banco.api_consulta.adapter.input.controller.dto.response.ContaSaldoResponse;
import br.com.banco.api_consulta.adapter.input.mapper.ContaInputMapper;
import br.com.banco.api_consulta.core.domain.model.Conta;
import br.com.banco.api_consulta.core.exception.ContaNaoEncontradaException;
import br.com.banco.api_consulta.core.exception.ServicoIndisponivelException;
import br.com.banco.api_consulta.port.input.ConsultarSaldoContaInputPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaController.class)
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarSaldoContaInputPort inputPort;

    @MockitoBean
    private ContaInputMapper mapper;

    @Test
    void deveRetornar200ComSaldoQuandoContaEncontrada() throws Exception {
        var numeroConta = "1369-8";
        var atualizadoEm = LocalDateTime.of(2023, 8, 13, 21, 11, 9);
        var conta = new Conta("1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);
        var response = new ContaSaldoResponse("1369-8", "Eduardo Campos Lima", "ATIVA", 385840.54, atualizadoEm);

        when(inputPort.consultar(numeroConta)).thenReturn(conta);
        when(mapper.toResponse(conta)).thenReturn(response);

        mockMvc.perform(get("/api/v1/contas/{numeroConta}/saldo", numeroConta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numConta").value("1369-8"))
                .andExpect(jsonPath("$.nomeCliente").value("Eduardo Campos Lima"))
                .andExpect(jsonPath("$.status").value("ATIVA"))
                .andExpect(jsonPath("$.saldo").value(385840.54));
    }

    @Test
    void deveRetornar404QuandoContaNaoEncontrada() throws Exception {
        var numeroConta = "9999-9";
        when(inputPort.consultar(numeroConta))
                .thenThrow(new ContaNaoEncontradaException(numeroConta));

        mockMvc.perform(get("/api/v1/contas/{numeroConta}/saldo", numeroConta))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Conta não encontrada"))
                .andExpect(jsonPath("$.mensagem").value("Conta não encontrada: " + numeroConta));
    }

    @Test
    void deveRetornar503QuandoCircuitBreakerAberto() throws Exception {
        var numeroConta = "1369-8";
        when(inputPort.consultar(numeroConta))
                .thenThrow(new ServicoIndisponivelException("gestao-conta"));

        mockMvc.perform(get("/api/v1/contas/{numeroConta}/saldo", numeroConta))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.erro").value("Serviço indisponível"))
                .andExpect(jsonPath("$.mensagem").value("Serviço indisponível no momento: gestao-conta"));
    }

    @Test
    void deveRetornar500QuandoOcorreErroInesperado() throws Exception {
        var numeroConta = "1369-8";
        when(inputPort.consultar(numeroConta))
                .thenThrow(new RuntimeException("Erro inesperado no serviço"));

        mockMvc.perform(get("/api/v1/contas/{numeroConta}/saldo", numeroConta))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.erro").value("Erro interno do servidor"));
    }
}