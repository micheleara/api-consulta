package br.com.banco.api_consulta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Consulta")
                        .description("Serviço responsável por realizar a consulta de saldo de contas bancárias")
                        .version("v1.0.0")
                );
    }
}