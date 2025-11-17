> ⚠️ **Aviso:** O projeto ainda **não está completo**.  
> As partes de **eleição de coordenador**, **servidor de referência** e **replicação entre servidores** não foram implementadas.  
> A documentação abaixo descreve o estado **parcial real** do sistema e o comportamento **planejado** onde relevante.

# Documentação do Projeto de Sistemas Distribuídos

## 1. Visão Geral do Projeto

Este projeto foi desenvolvido como parte da disciplina de Sistemas Distribuídos e implementa diversos conceitos essenciais, incluindo comunicação entre processos, relógios lógicos, troca de mensagens distribuídas, persistência e uma arquitetura envolvendo cliente, bot, broker, proxy e servidores.

Três linguagens foram usadas conforme exigido:
- **Java** — servidores, lógica de negócio, armazenamento.
- **JavaScript** — cliente e bot (geração de tráfego e testes).
- **Python** — broker e proxy (código base fornecido pelo professor).

As mensagens são armazenadas em **SQLite**, escolha livre permitida pelo enunciado.

Toda a execução é gerenciada com **Docker Compose**.

---

## 2. Estrutura do Sistema

### 2.1 Cliente (JavaScript)
- Implementado em JavaScript.
- Envia requisições aos servidores via broker/proxy.
- Utiliza padrão **request–reply**.
- Usa **relógio lógico** para timestamp de mensagens.

### 2.2 Bot (JavaScript)
- Simula atividades automáticas.
- Usa o mesmo protocolo do cliente.
- Envia mensagens públicas e privadas.

### 2.3 Broker e Proxy (Python)
- Código fornecido pelo professor.
- Realizam roteamento de mensagens entre cliente/bot ↔ servidores.
- Utilizam ZeroMQ para comunicação.

⚠️ Servidor de referência **não** foi implementado.

### 2.4 Servidores (Java)
- Contêm a lógica principal.
- Usam ZeroMQ.
- Implementam:
  - recepção e envio de mensagens
  - relógios lógicos
  - persistência com SQLite

⚠️ NÃO implementam:
- eleição de coordenador  
- replicação/sincronização entre servidores  
- servidor de referência  

---

## 3. Funcionalidades Implementadas

### 3.1 Request–Reply
Cliente e bot enviam requisições via broker/proxy e recebem respostas do servidor.

### 3.2 Serialização com MessagePack
Mensagens são compactadas usando MessagePack.

### 3.3 Relógios Lógicos
Todas as mensagens carregam timestamps lógicos para ordenação básica de eventos.

### 3.4 Persistência em SQLite
Mensagens públicas e privadas são armazenadas localmente em cada servidor.

---

## 4. Funcionalidades Não Implementadas

- ❌ Eleição de coordenador  
- ❌ Servidor de referência  
- ❌ Replicação entre servidores  
- ❌ Sincronização de estado  

---

## 5. Arquitetura Geral

Cliente (JS) → Broker (Python) → Proxy (Python) → Servidores (Java)  
Cliente e Bot usam os mesmos caminhos de comunicação.  
Todos os componentes executam em containers Docker.

---

## 6. Banco de Dados e Persistência

- Cada servidor mantém um banco SQLite independente.
- Persistência funciona corretamente.
- Não há replicação.

---

## 7. Instruções de Execução

**Limpar containers antigos:**  
docker compose down --remove-orphans

**Construir containers:**  
docker compose build

**Executar o cliente:**  
docker compose run --rm -it cliente

**Subir bots e demais serviços:**  
docker compose up

---

## 8. Critérios de Avaliação e Conformidade

### Cliente (2 pontos)
✔ ZeroMQ  
✔ Request–reply  
✔ Relógio lógico  

### Bot (1.5 ponto)
✔ Biblioteca correta  
✔ Troca de mensagens  

### Broker e Proxy (1 ponto)
✔ Funcionam  
❌ Servidor de referência não implementado  

### Servidor (4 pontos)
✔ ZeroMQ  
✔ Troca de mensagens  
✔ Relógio lógico  
❌ Eleição de coordenador  
❌ Replicação/sincronização  
❌ Servidor de referência  

### Documentação (0.5 ponto)
✔ Descrição clara e completa do que foi feito

### Apresentação (1 ponto)
✔ README adequado como apoio

---

## 9. Considerações Finais

O projeto implementa a base de um sistema distribuído de troca de mensagens, cobrindo comunicação via ZeroMQ, múltiplas linguagens, containers e persistência.

Apesar de algumas funcionalidades avançadas não terem sido concluídas, a estrutura atual demonstra os principais conceitos da disciplina e serve como base sólida para expansão futura.
