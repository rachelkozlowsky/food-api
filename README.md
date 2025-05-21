# FIAP Food - Sistema de Gerenciamento de Pedidos

Sistema para gerenciamento de pedidos de uma lanchonete, desenvolvido como parte do Tech Challenge da FIAP.

## Stack Tecnológica

- **Linguagem**: Kotlin
- **Framework**: Spring Boot 3.4.4
- **Banco de Dados**: MongoDB
- **Containerização**: Docker + Docker Compose
- **Documentação API**: Swagger/OpenAPI 3.0

## Arquitetura

O projeto utiliza Arquitetura (Quase) Hexagonal (Ports & Adapters) para garantir:

- Separação clara entre domínio e infraestrutura
- Independência de frameworks
- Facilidade de testes
- Baixo acoplamento

### Estrutura de Pacotes

```
com.fiap.food
├── adapter
│   ├── in
│   │   └── http        # Controllers REST
│   └── out
│       └── db          # Adaptadores de Persistência
├── application
│   ├── domain         # Entidades e Regras de Domínio
│   ├── port
│   │   ├── in         # Portas de Entrada (Use Cases)
│   │   └── out        # Portas de Saída (Repositories)
│   └── service       # Implementação dos Use Cases
└── config            # Configurações da Aplicação
```

## Funcionalidades

- Cadastro e gestão de clientes
- Gerenciamento de pedidos em fila
- Acompanhamento de status dos pedidos
- Integração com MongoDB para persistência
- Integração com MercadoPago para pagamentos
- Documentação da API via Swagger

## Como Executar

### Localmente

1. Clone o repositório
2. Certifique-se de ter Docker instalado
3. Execute o MongoDB:
   ```bash
   docker-compose up mongodb
   ```
4. Execute a aplicação com perfil local:
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=local
   ```

### Com Docker Compose

1. Clone o repositório
2. Execute:
   ```bash
   docker-compose up
   ```

## Documentação da API

A documentação da API está disponível via Swagger UI em:
- Local: http://localhost:8080/swagger-ui.html
- Docker: http://localhost:8080/swagger-ui.html

## Fluxo de Autenticação

O fluxo de autenticação do aplicativo segue os seguintes passos:

1. **Ponto de Entrada**:
   - O método `authenticate` da classe `AuthenticationService` é o ponto de entrada para autenticação.
   - Ele recebe um objeto `AuthenticationRequest` que pode ser de quatro tipos: `Anonymous`, `Login`, `Register` ou `Refresh`.

2. **Autenticação Anônima**:
   - Se o tipo for `Anonymous`, o método `createAnonymousUser` é chamado.
   - Um usuário temporário é criado com CPF, nome, e-mail e telefone gerados dinamicamente. O papel do usuário é definido como `ANONYMOUS`.

3. **Login**:
   - Se o tipo for `Login`, o método `authenticateRegisteredUser` é chamado.
   - O usuário é buscado no `UserService` pelo CPF. Se não for encontrado ou o telefone não corresponder, uma exceção `BadCredentialsException` é lançada.

4. **Registro**:
   - Se o tipo for `Register`, o método `registerUser` é chamado.
   - Verifica se já existe um usuário com o CPF fornecido. Caso exista, uma exceção é lançada.
   - Caso contrário, um novo usuário é criado e salvo no `UserService`.

5. **Geração de Tokens**:
   - Após autenticar ou registrar o usuário, um token de acesso (`accessToken`) e um token de atualização (`refreshToken`) são gerados pelo `TokenService`.
   - O `refreshToken` é salvo no `RefreshTokenRepository` junto com os detalhes do usuário.

6. **Resposta**:
   - O método retorna um objeto `AuthenticationResponse` contendo o `accessToken`, `refreshToken` e o CPF do usuário.

7. **Atualização de Token**:
   - O método `refreshAccessToken` é usado para renovar o token de acesso.
   - Ele valida o `refreshToken`, extrai o CPF e verifica se o token pertence ao usuário correto antes de gerar um novo `accessToken`.

8. **Segurança dos Tokens**:
   - O `TokenService` utiliza uma chave de assinatura (`signingKey`) para gerar e validar tokens JWT.
   - Os tokens incluem informações como CPF, papéis, tipo de usuário, nome, e-mail e telefone como claims.

### Resumo
O fluxo de autenticação garante:
- Criação de usuários anônimos com credenciais temporárias.
- Autenticação de usuários registrados com CPF e telefone.
- Registro de novos usuários com validação de CPF único.
- Geração e validação segura de tokens JWT para controle de acesso.

## Diagramas

- [Fluxo de autenticação](documentation/fluxo_autenticacao.png)
- [Cadastro e gestão de clientes](documentation/cadastro_e_gestao_clientes.png)
- [Cadastro e gestão de categorias](documentation/cadastro_e_gestao_categorias.png)
- [Cadastro e gestão de produtos](documentation/cadastro_e_gestao_produtos.png)
- [Cadastro e gestão de pedidos](documentation/cadastro_e_gestao_pedidos.png)
- [Cadastro e gestão de pagamentos](documentation/cadastro_e_gestao_pagamento.png)
