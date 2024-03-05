# API de Controle de Estoque

Esta é uma API de Controle de Estoque desenvolvida utilizando Java 17, Spring Boot 3.1.2, Maven e PostgreSQL. A API permite gerenciar informações relacionadas a produtos e estoque.

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- Java 17
- Maven
- PostgreSQL

## Configuração do Banco de Dados

1. Crie um banco de dados no PostgreSQL com o nome `estoque_api`.

2. Configure as credenciais do banco de dados no arquivo `src/main/resources/application.yml`.

```
spring:
  jpa:
    database: POSTGRESQL
    show-sql: true
    hibernate:
      ddl-auto: update
  datasource:
    platform: postgres
    url: jdbc:postgresql://localhost:5432/estoque_api
    username: your_user
    password: your_user_password
    driverClassName: org.postgresql.Driver
```
## Executando o Projeto
1. Clone este repositório para o seu ambiente local:
```
- git clone https://github.com/carlosalvessdev/gcp-devops-cert-train.git
```
2. Navegue até o diretório do projeto:
```
- cd gcp-devops-cert-train\back-end
```
3. Compile e execute o projeto usando o Maven:
```
- mvn clean install spring-boot:run
```