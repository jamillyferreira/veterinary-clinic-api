# API REST - Clínica 

API REST para gerenciamento de uma clínica veterinária, desenvolvida com Java e Spring Boot.
Permite o cadastro e gerenciamento de tutores, pets, veterinários e consultas com regras de negócio reais como controle de conflito de horários efluxo de status de atendimento.

## Tecnologias utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Specifications
* Spring Validation
* H2 Database
* Maven

## Como executar

```shell
# Clone o repositório
git clone https://github.com/jamillyferreira/veterinary-clinic-api

# Acesse o diretório
cd veterinary-clinic-api

# Execute a aplicação
./mvnw spring-boot:run
```

* A API estará disponível em: http://localhost:8080 
* O console do H2 pode ser acessado em: http://localhost:8080/h2-console
* A documentação interativa (Swagger UI) pode ser acessada em: http://localhost:8080/swagger-ui.html

---

## Endpoints

### Tutores

| Método | Endpoint            | Descrição                      | Status Code   |
|--------|---------------------|--------------------------------|---------------|
| POST   | `/tutors`           | Cadastrar novo tutor           | 201, 400, 409 |
| GET    | `/tutors`           | Listar todos os tutores        | 200, 500      |
| GET    | `/tutors/{id}`      | Buscar tutor por ID            | 200, 404      |
| GET    | `/tutors/cpf/{cpf}` | Buscar tutor por CPF           | 200, 404      |
| PATCH  | `/tutors/{id}`      | Atualizar tutor (parcialmente) | 200, 400, 404 |
| DELETE | `/tutors/{id}`      | Remover tutor                  | 204, 404, 500 |

---

### Pets

| Método | Endpoint          | Descrição                    | Status Code   |
|--------|-------------------|------------------------------|---------------|
| POST   | `/pets`           | Cadastrar novo pet           | 201, 400, 404 |
| GET    | `/pets`           | Listar todos os pets         | 200, 404      |
| GET    | `/pets/{id}`      | Buscar pet por ID            | 200, 404      |
| PATCH  | `/pets/{id}`      | Atualizar pet (parcialmente) | 200, 400, 404 |
| DELETE | `/pets/{id}`      | Remover pet                  | 204, 404,     |

Obs: Listrar todos os pets possui filtro por tutor opcional

---

### Veterinários

| Método | Endpoint                      | Descrição                            | Status Code   |
|--------|-------------------------------|--------------------------------------|---------------|
| POST   | `/veterinary`                 | Cadastrar veterinário                | 201, 400, 422 |
| GET    | `/veterinary`                 | Listar todos os veterinário          | 200           |
| GET    | `/veterinary/{id}`            | Buscar veterinário por ID            | 200, 404      |
| PATCH  | `/veterinary/{id}`            | Atualizar veterinário (parcialmente) | 200, 400, 404 |
| PATCH  | `/veterinary/{id}/activate`   | Ativar veterinário                   | 204, 404      |
| PATCH  | `/veterinary/{id}/deactivate` | Desativar veterinário                | 204, 404      |

---

### Consultas

| Método | Endpoint                           | Descrição                    | Status Code        |
|--------|------------------------------------|------------------------------|--------------------|
| POST   | `/appointments`                    | Agendar consulta             | 201, 400, 404      |
| GET    | `/appointments`                    | Listar todos as consultas    | 200, 404           |
| GET    | `/appointments/{id}`               | Buscar por ID                | 200, 404           |
| PATCH  | `/appointments/{id}/status`        | Atualizar status da consulta | 200, 400, 404, 422 |
| DELETE | `/appointments/{id}/clinical-data` | Concluir consulta            | 204, 404, 422      |

---

## Regras de negócio implementadas

### Integridade de dados

* CPF único por tutor — duplicatas retornam 409 Conflict
* CRMV único por veterinário
* Pet não pode existir sem tutor válido associado

### Restrições de deleção

* Tutor com pets cadastrados não pode ser removido
* Pet com consultas registradas não pode ser removido

### Veterinários

* Veterinário inativo não pode receber novos agendamentos
* Conflito de horário é verificado antes de confirmar agendamento

---

## Fluxo de consultas

`AGENDADA → EM_ATENDIMENTO → CONCLUIDA`

* Diagnóstico só pode ser registrado em consultas `EM_ATENDIMENTO` — ao registrar, o status muda automaticamente para `CONCLUIDA`
* Cancelamento só permitido em consultas com status `AGENDADA`
* Agendamento em data/hora passada é rejeitado

---

## Limitações conhecidas

* Banco de dados em memória (H2) — sem persistência entre execuções
* Sem autenticação/autorização (Spring Security não implementado)

---

## Próximos passos

Este projeto está em evolução contínua. As melhorias planejadas seguem uma ordem intencional de aprendizado:

* Integração com banco de dados real — migrar de H2 para MySQL, com gerenciamento de schema via Flyway
* Testes unitários — cobertura da camada de serviço com JUnit 5 e Mockito

---

## O que aprendi 

#### Enum com corpo de classe por constante (padrão State)
Implementei o fluxo de status das consultas usando enums com métodos abstratos, onde cada constante define suas próprias transições válidas. 
Isso mantém a lógica de negócio encapsulada no próprio domínio, evitando condicionais espalhadas na camada de serviço.

#### Filtros dinâmicos com Specification
Ao implementar os filtros do endpoint de listagem de consultas, percebi que os múltiplos if/else para combinar parâmetros opcionais estavam me incomodando — parecia que havia uma forma melhor. 
Pesquisei e encontrei o padrão Specification do JPA, estudei e apliquei pela primeira vez nesse projeto para compor filtros de consulta (por pet, veterinário e status) de forma dinâmica, sem proliferar métodos no repository para cada combinação de parâmetros.

#### Tratamento centralizado de exceções
Pratiquei o uso do `@ControllerAdvice` para centralizar o tratamento de erros, separando essa responsabilidade dos controllers e padronizando as respostas de erro da API.

#### Validação em camadas
Entendi a diferença entre validar formato e presença de dados com `@Valid` no controller, e validar regras de negócio (como conflito de horário e status inválido) na camada de serviço.

#### Mapeamento de relacionamentos com JPA 

Pratiquei `@OneToMany` e `@ManyToOne` e comecei a entender as implicações de carregamento lazy vs eager no comportamento das queries.


---
## Estrutura do projeto

```mermaid
src/
└── main/
    └── java/
        └── com/vetclinic/
            ├── controller/
            ├── service/
            ├── repository/
            ├── model/
            ├── dto/
            └── exception/
```
---

## Autora
Jamilly Ferreira

[LinkedIn](https://www.linkedin.com/in/jamillyferreira/) | [GitHub](https://github.com/jamillyferreira) | [Instagram](https://www.instagram.com/dev_jamilly?igsh=bmc4YXAweXNjMzR5)

