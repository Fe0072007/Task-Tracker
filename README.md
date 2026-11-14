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

A execução pelo JAR exige Java instalado. Para usar o comando `task-cli` com
um runtime incluído, utilize o instalador Windows descrito abaixo.

## Instalador Windows

### Gerar o instalador

O script [build-installer.ps1](build-installer.ps1) compila o projeto, executa
os testes e gera um instalador `.exe` com o runtime Java incluído.

Na máquina que gera o instalador, são necessários:

- Windows e PowerShell.
- Maven e um JDK disponíveis para a compilação.
- `jpackage`, configurado na variável `$jpackage` do script. Atualmente o caminho
  é `C:\Program Files\Java\jdk-27\bin\jpackage.exe`; ajuste-o se a instalação
  estiver em outro local. O runtime distribuído vem desse JDK, embora o projeto
  tenha Java 17 como alvo de compilação.
- WiX Toolset compatível com o `jpackage` utilizado, disponível no `PATH`, e
  seus termos aceitos conforme a versão instalada. Consulte a
  [documentação de aceitação do WiX](https://docs.firegiant.com/wix/osmf/).

Na pasta `task-cli`, execute:

```powershell
.\build-installer.ps1
```

Se a política de execução bloquear scripts locais, você pode executar esta
cópia revisada do script com uma política limitada ao novo processo:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\build-installer.ps1
```

O script executa `mvn clean package`, recria `dist-input` com o JAR e o wrapper
`task-cli.cmd`, e grava o instalador em `dist-installer`. A pasta `dist-input`
é descartável: seu conteúdo anterior é removido a cada execução.

Os arquivos em [installer-resources](installer-resources) fornecem o wrapper
do comando e a personalização WiX que adiciona a pasta `app` da instalação ao
`PATH` do sistema. Preserve esses recursos ao gerar o pacote.

### Instalar e conferir

1. Localize o `.exe` gerado em `dist-installer` e execute-o.
2. Conclua o assistente e autorize a elevação solicitada pelo Windows. O script
   atual gera uma instalação para a máquina e altera o `PATH` do sistema.
3. Feche e abra novamente o terminal para carregar o `PATH` atualizado.
4. Confira a instalação:

```powershell
where.exe task-cli
task-cli --version
task-cli --help
```

Na versão atual, `--version` deve exibir `Task CLI v1.1.1`. O computador que
recebe o instalador não precisa ter Java ou Maven instalados. O aplicativo é
usado pelo terminal, passando os comandos descritos abaixo.

Se `task-cli` não for encontrado, verifique se o `PATH` contém a subpasta `app`
do diretório escolhido no assistente. Ela contém `task-cli.cmd`, que encaminha
os argumentos para `Task Tracker CLI.exe` na pasta superior. Se estiver usando
o terminal integrado de uma IDE, reabra a IDE para atualizar o ambiente.

Para desinstalar, use a lista de aplicativos instalados nas Configurações do
Windows. O JSON de tarefas fica no diretório de dados descrito em
[Armazenamento](#armazenamento), separado dos arquivos da aplicação.

### Versão do pacote

A versão atual é `1.1.1`, consistente nos seguintes arquivos:

| Arquivo | Definição |
| --- | --- |
| `pom.xml` | Versão Maven do projeto: `1.1.1`. |
| `build-installer.ps1` | `$version = "1.1.1"`, usada no nome do JAR e em `--app-version`. |
| `VersionCommand.java` | Versão apresentada por `--version`: `1.1.1`. |

O template `installer-resources/main.wxs` recebe a versão do `jpackage` pela
variável `JpAppVersion`; ele não mantém outra versão fixa. Ao publicar uma nova
versão, atualize as três definições acima e os exemplos deste README.

## Comandos

Após instalar, use `task-cli` seguido de um dos comandos abaixo. Para executar
pelo JAR, substitua `task-cli` por `java -jar target/task-cli-1.1.1.jar`.

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
task-cli add "Estudar Java"
task-cli list-all
task-cli update 1 "Estudar Java e Maven"
task-cli list-todo
task-cli mark-progress 1
task-cli list-in-progress
task-cli mark-done 1
task-cli list-done
task-cli delete 1
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
