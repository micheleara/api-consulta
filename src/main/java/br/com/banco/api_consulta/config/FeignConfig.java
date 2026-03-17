package br.com.banco.api_consulta.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.banco.api_consulta.adapter.output.client")
public class FeignConfig {}