# Task Tracker CLI

Projeto Java 17, gerenciado com Maven.

## Arquitetura

- Main: inicializa a aplicação e suas dependências.
- CommandParser: interpreta os argumentos da CLI.
- Command: contrato para os comandos.
- Commands: executam operações através do Service.
- Service: concentra as operações e regras de negócio.
- Repository: responsável pela persistência em JSON.

## Regras

- Evitar lógica de negócio no Main.
- Não acessar diretamente o Repository a partir dos Commands.
- Manter mensagens de erro amigáveis na CLI.
- Preservar a separação de responsabilidades.
- Explicar mudanças arquiteturais e pedir autorização antes de implementá-las.