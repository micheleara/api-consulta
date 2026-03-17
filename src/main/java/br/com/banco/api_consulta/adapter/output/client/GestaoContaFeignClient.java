package br.com.banco.api_consulta.adapter.output.client;

import br.com.banco.api_consulta.adapter.output.client.dto.response.GestaoContaClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gestao-conta", url = "${clients.gestao-conta.url}")
public interface GestaoContaFeignClient {

    @GetMapping("/api/v1/consulta-contas")
    GestaoContaClientResponse consultarConta(@RequestParam("num_conta") String numConta);
}