# *Sistema Bancário*

Sistema bancário em Java 21 executado no terminal, que simula as operações básicas de um banco. O projeto foi construído com foco em orientação a objetos, separação de responsabilidades e código limpo, sem uso de frameworks.

Status: em desenvolvimento. A seção Roadmap mostra o que já está pronto e o que falta.

Sumário
Funcionalidades
Tecnologias
Arquitetura
Estrutura de pastas
Conceitos de POO aplicados
Decisões de design
Como executar
Testes
Roadmap
Possíveis melhorias futuras
Autora
Funcionalidades

Clientes

Cadastro com nome, CPF e data de nascimento
Validação de CPF (dígitos verificadores) e bloqueio de CPF duplicado
Listagem de clientes

Contas

Criação de Conta Corrente e Conta Poupança
Número da conta gerado automaticamente
Cada conta possui número, agência, saldo, titular e histórico de movimentações
Listagem de contas e consulta de dados da conta

Operações

Depositar
Sacar
Transferir entre contas
Consultar saldo
Exibir extrato completo

Regras de negócio

Não permite saque maior que o saldo disponível
Não permite transferência para conta inexistente
Não permite valores negativos ou zerados
Não permite cadastro duplicado de CPF

Histórico

Cada operação gera uma Transacao com identificador único, tipo, valor, data e hora, conta de origem e conta de destino (quando houver)
Tecnologias
Tecnologia	Uso
Java 21 (LTS)	Linguagem
Maven	Build e gerenciamento de dependências
JUnit 5	Testes unitários
Git / GitHub	Versionamento

Não utiliza Spring Boot nem outro framework: a intenção é demonstrar domínio da linguagem e dos fundamentos.

Arquitetura

O projeto é dividido em camadas, com dependência em uma única direção:

app  →  service  →  repository  →  model
 └────────┴───────────┴────────────→  exception, util (transversais)
Camada	Responsabilidade
app	Ponto de entrada (Main) e interface de terminal (Menu). Não contém regra de negócio.
service	Regras de negócio e orquestração entre repositórios e modelos.
repository	Armazenamento e busca de dados, usando HashMap.
model	Entidades do domínio, com seus dados e comportamentos próprios.
exception	Exceções personalizadas de regra de negócio.
util	Geradores de identificadores e validações.
Estrutura de pastas
sistema-bancario/
├── pom.xml
├── README.md
└── src/
    ├── main/java/br/com/sistemabancario/
    │   ├── app/          Main, Menu
    │   ├── service/      ClienteService, ContaService, TransacaoService
    │   ├── repository/   ClienteRepository, ContaRepository
    │   ├── model/        Pessoa, Cliente, Conta, ContaCorrente,
    │   │                 ContaPoupanca, Transacao, TipoTransacao, Banco
    │   ├── exception/    NegocioException e exceções específicas
    │   └── util/         GeradorNumeroConta, GeradorNumeroTransacao, Validador
    └── test/java/br/com/sistemabancario/
        ├── model/
        ├── service/
        └── util/
Conceitos de POO aplicados
Conceito	Onde aparece
Encapsulamento	Atributos privados; o saldo só muda por depositar e sacar; Transacao é imutável
Herança	Cliente herda de Pessoa; ContaCorrente e ContaPoupanca herdam de Conta; as exceções herdam de NegocioException
Classe abstrata	Conta e Pessoa não podem ser instanciadas diretamente
Polimorfismo	O saque usa saldoDisponivel(), que cada tipo de conta implementa à sua maneira
Sobrescrita	ContaCorrente e ContaPoupanca sobrescrevem o comportamento da Conta
Sobrecarga	Transacao possui construtores com assinaturas diferentes
Composição	Uma Conta possui seu histórico de Transacao, que não existe fora dela
Agregação	Um Cliente referencia suas Contas, que também existem no banco
Associação	Uma Conta conhece seu titular (Cliente)
Interfaces	Contratos entre camadas, quando fizer sentido
Decisões de design
BigDecimal para dinheiro. double não representa valores decimais com exatidão (0.1 + 0.2 resulta em 0.30000000000000004). Em sistemas financeiros, o correto é BigDecimal.
Exceções checked. As exceções de negócio estendem Exception, o que obriga o tratamento explícito em operações financeiras.
Conta é classe abstrata, não interface. Ela tem estado (saldo, histórico) e comportamento compartilhado, o que é papel de classe abstrata.
Regras próprias da conta ficam na Conta. sacar() e depositar() pertencem ao objeto, o que permite polimorfismo. O service orquestra o que envolve mais de uma conta, como a transferência.
Transacao imutável. Uma operação que já aconteceu não pode ser alterada. Ela guarda os números das contas, e não os objetos, para evitar dependência circular.
HashMap nos repositórios. Busca por chave (CPF ou número da conta) em tempo O(1).
Geradores como classes de instância. Sem estado estático, o que facilita os testes e a futura persistência.
Como executar

Pré-requisitos

JDK 21 (java -version e javac -version devem mostrar a versão 21)
Maven 3.6 ou superior (mvn -version)
Git

Passo a passo

bash
# 1. Clonar o repositório
git clone https://github.com/MariaEduardaAlves835/sistema-bancario.git
cd sistema-bancario

# 2. Compilar
mvn clean compile

# 3. Executar (disponível quando a camada app estiver concluída)
mvn exec:java
Testes
bash
mvn test

Os testes unitários usam JUnit 5 e ficam em src/test/java, espelhando a estrutura dos pacotes de src/main/java.

Roadmap
 Configuração do projeto (Maven, Java 21, JUnit 5)
 Pacote exception
 Pacote util (geradores e validador de CPF)
 Pacote model
 TipoTransacao, Transacao, Pessoa
 Conta, ContaCorrente, ContaPoupanca
 Cliente, Banco
 Pacote repository
 Pacote service
 Pacote app (menu interativo)
 Persistência em arquivo
 Logs das operações
 Cobertura de testes dos services
Possíveis melhorias futuras
Persistência em arquivos (.txt ou .json) para manter os dados entre execuções
Registro de logs das operações
Rendimento mensal da conta poupança e tarifas da conta corrente
Autenticação por senha ou PIN
Banco de dados relacional com JDBC
API REST com Spring Boot reaproveitando as camadas de domínio e serviço
Interface gráfica ou web
Autora

Maria Eduarda Alves GitHub: MariaEduardaAlves835
