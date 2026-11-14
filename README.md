# Task Tracker CLI

Aplicação de terminal para gerenciar tarefas, desenvolvida em Java 17 com Maven.
Permite adicionar, atualizar, excluir e listar tarefas, além de marcar seu progresso.
Os dados são armazenados localmente em JSON usando Jackson.

## Pré-requisitos

- JDK 17 ou superior, com `java` disponível no terminal.
- Maven, com `mvn` disponível no terminal.
- Acesso à internet na primeira compilação para baixar dependências e plugins.

Confira as instalações:

```sh
java -version
mvn -version
```

## Compilação e execução

Execute os comandos abaixo na pasta `task-cli`, onde está o `pom.xml`:

```sh
mvn package
java -jar target/task-cli-1.1.1.jar --help
```

O comando `package` compila o projeto, executa os testes e gera um JAR com as
dependências. Se a versão do projeto mudar, ajuste o nome do JAR nos exemplos.

Também é possível executar pelo Maven:

```sh
mvn compile exec:java -Dexec.args="list-all"
```

A ajuda usa `task-cli` como nome da aplicação. Nos exemplos deste README, a
invocação completa é `java -jar target/task-cli-1.1.1.jar`.

## Comandos

Acrescente um dos comandos abaixo à invocação do JAR:

| Comando | Ação |
| --- | --- |
| `add "descrição"` | Adiciona uma tarefa com status `TODO`. |
| `update <id> "descrição"` | Atualiza a descrição da tarefa. |
| `delete <id>` | Exclui a tarefa. |
| `mark-progress <id>` | Altera o status para `IN_PROGRESS`. |
| `mark-done <id>` | Altera o status para `DONE`. |
| `list-all` | Lista todas as tarefas. |
| `list-todo` | Lista tarefas com status `TODO`. |
| `list-in-progress` | Lista tarefas com status `IN_PROGRESS`. |
| `list-done` | Lista tarefas com status `DONE`. |
| `--help` | Exibe a ajuda. |
| `--version` | Exibe a versão. |

Exemplo de uso; substitua `1` pelo ID retornado ao adicionar:

```sh
java -jar target/task-cli-1.1.1.jar add "Estudar Java"
java -jar target/task-cli-1.1.1.jar update 1 "Estudar Java e Maven"
java -jar target/task-cli-1.1.1.jar mark-progress 1
java -jar target/task-cli-1.1.1.jar list-in-progress
java -jar target/task-cli-1.1.1.jar mark-done 1
java -jar target/task-cli-1.1.1.jar list-done
java -jar target/task-cli-1.1.1.jar delete 1
```

Use aspas em descrições com espaços. Descrições vazias ou compostas apenas por
espaços são rejeitadas. Os IDs devem ser números inteiros positivos dentro do
intervalo de `long`. Executar sem argumentos exibe a ajuda.

## Armazenamento

O arquivo `tasks.json` fica em:

- `%APPDATA%/TaskCLI/tasks.json`, quando `APPDATA` está definida e não está vazia.
- `TaskCLI/tasks.json` dentro da pasta pessoal informada por `user.home`, quando
  `APPDATA` está ausente ou vazia.

A pasta e o arquivo são criados automaticamente ao acessar a persistência.
Cada tarefa contém ID, descrição, status e datas de criação e atualização,
representadas no código por `OffsetDateTime`.

As alterações são gravadas primeiro em um arquivo temporário na mesma pasta.
Após a escrita, o repositório solicita a substituição atômica do JSON original.
Se a movimentação atômica não for suportada, a operação informa uma falha;
não há tentativa automática de movimentação comum.

Falhas de I/O são comunicadas por `PersistenceException`, preservando a causa
original. O `Main` apresenta uma mensagem em `System.err`, sem stack trace.

## Arquitetura

```text
Main → CommandParser → Command concreto → ITaskService → ITaskRepository
                                              ↓                ↓
                                         TaskService     TaskRepository → JSON
```

| Componente | Responsabilidade |
| --- | --- |
| `Main` | Monta as dependências e executa o comando, tratando falhas de persistência. |
| `CommandParser` | Interpreta argumentos e valida o formato dos IDs. |
| `Command` | Define o método abstrato `execute()` dos comandos. |
| Comandos concretos | Chamam o serviço e apresentam os resultados. |
| `TaskPrinter` | Centraliza a impressão das tabelas de tarefas. |
| `TaskService` | Valida descrições e coordena operações pelo repositório. |
| `TaskRepository` | Lê e grava as tarefas em JSON com Jackson. |
| `Task` e `Status` | Representam os dados e os estados das tarefas. |

O serviço e o parser recebem suas dependências por construtor. O repositório
também aceita um diretório pelo construtor, permitindo testes isolados; o
construtor sem argumentos mantém o armazenamento padrão. Não há opção de CLI
para configurar esse diretório.

Consulte [AGENTS.md](AGENTS.md) antes de alterar a arquitetura. Os comandos
acessam o serviço, e o serviço acessa o repositório.

## Testes

```sh
mvn test
```

Os testes usam JUnit 5, executados pelo Maven Surefire:

- `TaskServiceTest`: descrições inválidas sem acesso ao repositório, exclusão
  da última tarefa, tarefas inexistentes e propagação de falhas de persistência.
- `TaskRepositoryTest`: ciclo de persistência, consultas por status, JSON
  inválido, falha na criação do diretório e preservação do original com limpeza
  do temporário após falha de serialização.
- `MainTest`: mensagem única de erro de persistência na CLI, sem stack trace.

Os testes do repositório e da CLI usam diretórios temporários, sem acessar o
arquivo de tarefas do usuário. Os relatórios são gerados em
`target/surefire-reports`.

## Limitações atuais

- O ID é calculado como o maior ID existente mais um. Excluir a tarefa de maior
  ID permite reutilizá-lo; excluir todas reinicia a contagem em `1`.
- Cada operação carrega o JSON inteiro, e cada alteração regrava a lista inteira.
- Não há bloqueio entre processos. Duas execuções simultâneas podem perder
  alterações ou gerar IDs repetidos.
- A movimentação atômica não garante durabilidade em uma queda de energia.
- Não há comando para retornar uma tarefa ao status `TODO`.
