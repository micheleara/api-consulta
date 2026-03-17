# api-consulta

![Versão](https://img.shields.io/badge/versão-0.0.1--SNAPSHOT-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

Microsserviço responsável por consultar o saldo de contas bancárias. Atua como agregador de dados, consumindo o serviço externo `gestao-conta` e expondo uma API REST padronizada para os clientes internos do banco.

---

## Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso](#uso)
- [API](#api)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuição](#contribuição)
- [Roadmap](#roadmap)

---

## Visão Geral

O `api-consulta` é um microsserviço da plataforma bancária que centraliza a consulta de saldo de contas correntes. Ele abstrai a complexidade de integração com o serviço legado `gestao-conta`, entregando uma API REST versionada, documentada e resiliente para ser consumida por outros serviços ou front-ends.

**Principais funcionalidades:**

- Consulta de saldo por número de conta via `GET /api/v1/contas/{numeroConta}/saldo`
- Integração com o serviço externo `gestao-conta` via OpenFeign
- Circuit Breaker e Retry automático com Resilience4j
- Tratamento de erros padronizado com respostas HTTP semânticas (404, 503, 500)
- Documentação interativa via Swagger UI

---

## Tecnologias

| Categoria       | Tecnologia                        | Versão   |
|-----------------|-----------------------------------|----------|
| Linguagem       | Java                              | 21       |
| Framework       | Spring Boot                       | 3.5.11   |
| HTTP Client     | Spring Cloud OpenFeign            | 4.2.1    |
| Resiliência     | Resilience4j                      | 2.2.0    |
| Documentação    | SpringDoc OpenAPI (Swagger UI)    | 2.8.16   |
| Monitoramento   | Spring Boot Actuator + Micrometer | —        |
| Testes          | JUnit 5 + Mockito + AssertJ       | —        |
| Cobertura       | JaCoCo                            | —        |
| Build           | Maven                             | 3.x      |

---

## Arquitetura

O serviço segue a **Arquitetura Hexagonal (Ports and Adapters)**, garantindo isolamento total entre o núcleo de negócio e os detalhes de infraestrutura.

```
┌─────────────────────────────────────────────────────────────────┐
│                        ADAPTER INPUT                            │
│   ContaController  ──→  ConsultarSaldoContaInputPort            │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                    ┌──────────▼──────────┐
                    │        CORE         │
                    │                     │
                    │  ConsultarSaldo     │
                    │    ContaUseCase     │
                    │                     │
                    │  domain/model/Conta │
                    └──────────┬──────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│                        ADAPTER OUTPUT                           │
│   GestaoContaClient  ──→  ConsultarSaldoContaOutputPort         │
│   GestaoContaFeignClient  →  gestao-conta (HTTP :8081)          │
│   Circuit Breaker + Retry (Resilience4j)                        │
└─────────────────────────────────────────────────────────────────┘
```

**Fluxo de resiliência:**

```
Requisição
  └── CircuitBreaker (aberto?) ──SIM──→ ServicoIndisponivelException → 503
              │
             NÃO
              │
          Retry (até 3x, backoff exponencial: 500ms → 1s → 2s)
              │
        Feign → gestao-conta (timeout: connect 3s / read 5s)
              ├── 404 → ContaNaoEncontradaException → 404
              ├── 5xx → Retry → esgotou → Fallback → 503
              └── 200 → Conta → ContaSaldoResponse → 200
```

---

## Pré-requisitos

- Java >= 21
- Maven >= 3.8 (ou usar o wrapper `./mvnw` incluso)
- Serviço `gestao-conta` rodando em `http://localhost:8081`


---

## Configuração

As configurações ficam em `src/main/resources/application.yaml`.

| Propriedade                                | Descrição                                      | Obrigatória | Padrão                  |
|--------------------------------------------|------------------------------------------------|-------------|-------------------------|
| `server.port`                              | Porta HTTP do serviço                          | Não         | `8085`                  |
| `clients.gestao-conta.url`                 | URL base do serviço externo gestao-conta       | Sim         | `http://localhost:8081` |
| `feign.client.config.gestao-conta.connectTimeout` | Timeout de conexão com gestao-conta (ms) | Não    | `3000`                  |
| `feign.client.config.gestao-conta.readTimeout`    | Timeout de leitura com gestao-conta (ms) | Não    | `5000`                  |
| `resilience4j.circuitbreaker.instances.gestao-conta.failureRateThreshold` | % de falhas para abrir o circuit breaker | Não | `50` |
| `resilience4j.circuitbreaker.instances.gestao-conta.waitDurationInOpenState` | Tempo com circuit breaker aberto | Não | `15s` |
| `resilience4j.retry.instances.gestao-conta.maxAttempts` | Número máximo de tentativas | Não | `3` |

---

## Uso

```bash
# Iniciar a aplicação
./mvnw spring-boot:run
```

### Exemplo de requisição

```bash
curl -X GET http://localhost:8085/api/v1/contas/1369-8/saldo
```

**Resposta de sucesso (200):**

```json
{
  "numConta": "1369-8",
  "nomeCliente": "Eduardo Campos Lima",
  "status": "ATIVA",
  "saldo": 385840.54,
  "atualizadoEm": "2023-08-13T21:11:09"
}
```

**Resposta de erro — conta não encontrada (404):**

```json
{
  "status": 404,
  "erro": "Conta não encontrada",
  "mensagem": "Conta não encontrada: 9999-9",
  "timestamp": "2026-03-17T10:30:00"
}
```

**Resposta de erro — serviço indisponível (503):**

```json
{
  "status": 503,
  "erro": "Serviço indisponível",
  "mensagem": "Serviço indisponível no momento: gestao-conta",
  "timestamp": "2026-03-17T10:30:00"
}
```

---

## API

Base URL: `http://localhost:8085/api/v1`

| Método | Endpoint                        | Descrição                                    |
|--------|---------------------------------|----------------------------------------------|
| `GET`  | `/contas/{numeroConta}/saldo`   | Consulta saldo e dados da conta pelo número  |

Documentação interativa (Swagger UI): `http://localhost:8085/swagger-ui/index.html`

Monitoramento (Actuator):

| Endpoint                        | Descrição                              |
|---------------------------------|----------------------------------------|
| `GET /actuator/health`          | Status geral + estado do circuit breaker |
| `GET /actuator/circuitbreakers` | Métricas detalhadas do circuit breaker |
| `GET /actuator/retries`         | Contadores de retry                    |
| `GET /actuator/metrics`         | Métricas gerais da aplicação           |

---

## Estrutura do Projeto

```
api-consulta/
├── src/
│   ├── main/
│   │   ├── java/br/com/banco/api_consulta/
│   │   │   ├── core/
│   │   │   │   ├── domain/model/        # Entidade de domínio (Conta)
│   │   │   │   ├── usecase/             # Casos de uso (sem anotações externas)
│   │   │   │   └── exception/           # Exceptions de domínio
│   │   │   ├── port/
│   │   │   │   ├── input/               # Interfaces de entrada (InputPort)
│   │   │   │   └── output/              # Interfaces de saída (OutputPort)
│   │   │   ├── adapter/
│   │   │   │   ├── input/
│   │   │   │   │   ├── controller/      # REST Controllers + GlobalExceptionHandler
│   │   │   │   │   │   └── dto/response # DTOs de resposta HTTP
│   │   │   │   │   └── mapper/          # Mapeamento domain → response DTO
│   │   │   │   └── output/
│   │   │   │       ├── client/          # Feign Client para gestao-conta
│   │   │   │       │   └── dto/response # DTOs do serviço externo
│   │   │   │       └── mapper/          # Mapeamento client response → domain
│   │   │   └── config/                  # Beans de configuração (UseCase, Feign, OpenAPI)
│   │   └── resources/
│   │       └── application.yaml         # Configurações da aplicação
│   └── test/                            # Testes unitários e de integração web
├── .documento/                          # Documentação interna do projeto
├── pom.xml                              # Dependências e plugins Maven
└── README.md
```
