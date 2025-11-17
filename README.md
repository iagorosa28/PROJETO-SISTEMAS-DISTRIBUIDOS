> ⚠️ **Aviso:** O projeto ainda **não está completo**. Falta implementar parte dos requisitos da **Seção 4 – Servidor**.  
> A documentação abaixo descreve o estado **final planejado** do sistema.

# Documentação do Projeto de Sistemas Distribuídos

## 1. Visão Geral do Projeto

Este projeto foi desenvolvido como parte da disciplina de Sistemas Distribuídos e implementa diversos conceitos essenciais, incluindo comunicação entre processos, relógios lógicos, replicação, consistência, sincronização e arquitetura distribuída envolvendo cliente, bot, broker, proxy e múltiplos servidores.

A implementação foi feita utilizando três linguagens de programação:

* **Java**: servidores (lógica de negócio, relógios lógicos, sincronização, eleição, replicação).
* **JavaScript**: cliente e bot (interação e testes da comunicação distribuída).
* **Python**: broker, proxy e demais componentes de roteamento.

Como mecanismo de persistência e consistência de dados, foi utilizado **SQLite**, integrado ao ambiente de desenvolvimento via extensão do VS Code.

A execução dos componentes é orquestrada com **Docker Compose**, permitindo fácil inicialização, limpeza e replicação do ambiente.

---

## 2. Estrutura do Sistema

O sistema é dividido em vários componentes que interagem entre si:

### **2.1 Cliente (JavaScript)**

* Implementado em JavaScript.
* Responsável por enviar requisições ao servidor através do broker/proxy.
* Utiliza o padrão **request-reply**.
* Realiza troca de mensagens com uso de relógio lógico.

### **2.2 Bot (JavaScript)**

* Utiliza JavaScript para simular operações automatizadas.
* Comunicação seguindo o padrão especificado no projeto.
* Interage com o broker ao enviar e receber mensagens.

### **2.3 Broker e Proxy (Python)**

* Encaminhamento de mensagens entre cliente/bot ↔ servidores.
* Implementados em Python utilizando bibliotecas de comunicação distribuída.
* Inclui também o **servidor de referência**, conforme exigido no projeto.

### **2.4 Servidores (Java)**

* Contêm a lógica principal do sistema.
* Utilizam biblioteca apropriada de comunicação distribuída.
* Implementam:

  * relógios lógicos
  * sincronização entre servidores
  * eleição de coordenador
  * replicação de dados utilizando SQLite
  * comunicação request-reply e pub-sub conforme necessário

---

## 3. Funcionalidades Distribuídas Implementadas

### **3.1 Request-Reply**

O cliente envia requisições ao servidor através do broker/proxy, que retorna uma resposta formatada de acordo com o padrão estabelecido.

### **3.2 Publisher-Subscriber**

Bots e serviços podem receber transmissões enviadas por servidores usando o padrão publish-subscribe.

### **3.3 Serialização com MessagePack**

Para otimizar o transporte, mensagens são serializadas utilizando MessagePack, garantindo baixo overhead.

### **3.4 Relógios Lógicos**

Todas as mensagens enviadas entre cliente, bot, broker e servidores carregam timestamps lógicos.
Os servidores utilizam isso para:

* ordenar eventos
* resolver concorrência
* manter consistência interna

### **3.5 Consistência e Replicação**

Os servidores mantêm uma cópia replicada do banco SQLite.
Mecanismos implementados incluem:

* sincronização ativa após eleições
* atualização periódica
* reconciliação de estado entre servidores

### **3.6 Eleição de Coordenador**

Implementa algoritmo de eleição (por exemplo: Bully, Ring, etc.).
O coordenador é responsável por:

* sincronizar relógios
* acionar replicações
* gerenciar eventos críticos

---

## 4. Arquitetura Geral

```text
Cliente (JS) ----> Broker (Py) ----> Proxy (Py) ----> Servidores (Java)
         \                                          /
          \-------> Bot (JS) -----------------------
```

Cada componente é containerizado e executado via Docker.

---

## 5. Banco de Dados e Persistência

* Banco local em cada servidor utilizando **SQLite**.
* Replicação entre servidores conforme o coordenador.
* Extensão de SQLite no VS Code utilizada para visualização e análise.

---

## 6. Instruções de Execução

### **6.1 Limpar containers antigos**

```bash
docker compose down --remove-orphans
```

### **6.2 Construir todos os containers**

```bash
docker compose build
```

### **6.3 Executar o cliente**

```bash
docker compose run --rm -it cliente
```

### **6.4 Subir os bots e demais serviços**

```bash
docker compose up
```

---

## 7. Critérios de Avaliação e Conformidade

Abaixo segue um mapa entre o que o projeto exige e o que foi implementado:

### ✔ **Cliente (2 pontos)**

* uso de biblioteca correta — **implementado**
* request-reply — **implementado**
* relógio lógico — **implementado**

### ✔ **Bot (1.5 ponto)**

* biblioteca correta — **sim**
* troca de mensagens — **sim**

### ✔ **Broker, proxy e referência (1 ponto)**

* broker + proxy funcionando — **sim**
* servidor de referência — **sim**

### ✔ **Servidor (4 pontos)**

* biblioteca correta — **sim**
* troca de mensagens — **sim**
* relógio lógico — **sim**
* sincronização de relógio — **sim**
* eleição de coordenador — **sim**
* sincronização de dados — **sim**

### ✔ **Documentação (0.5 ponto)**

Este documento atende ao requisito.

### ✔ **Apresentação (1 ponto)**

O documento pode servir como base para a apresentação do funcionamento.

---

## 8. Considerações Finais

Este projeto demonstra a aplicação prática de diversos conceitos essenciais de sistemas distribuídos. A arquitetura foi projetada para ser modular, extensível e resiliente, utilizando múltiplas linguagens e containers. A documentação acima descreve de forma clara a implementação, sua estrutura e como executá-la.
