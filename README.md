# 🏥 Agendamento Médico

Este projeto é uma aplicação **Full Stack** para gerenciamento de um sistema de **Agendamento de Consultas Médicas**.  
Inclui **API RESTful (Spring Boot)** no backend e uma **interface web** moderna no frontend (HTML, CSS e JS).

A aplicação permite gerenciar:
- 👨‍⚕️ **Médicos** e suas especialidades
- 🧑‍🤝‍🧑 **Pacientes** e seus convênios
- 🩺 **Especialidades**
- 🏥 **Convênios**
- 📅 **Agendamentos de consultas médicas**

---

## 🚀 Funcionalidades

✅ Cadastro, edição, listagem e exclusão de **Médicos, Pacientes, Especialidades e Convênios**  
✅ Agendamento, remarcação e cancelamento de consultas  
✅ Validações de dados com mensagens de erro amigáveis  
✅ Layout com **Glassmorphism** para uma interface moderna e responsiva  
✅ Navegação entre páginas para cada módulo do sistema  
✅ Paginação na listagem de registros  
✅ Máscaras para campos (ex.: telefone) e validação de e-mail  
✅ Backend robusto com **Spring Boot + JPA + Hibernate**  
✅ Conexão com banco de dados relacional (H2/MySQL)  
✅ Documentação da API com **Swagger/OpenAPI**

---

## 🌐 Acesso Local

Após iniciar a aplicação (backend rodando), acesse no navegador:

http://localhost:8080/index.html

- Página principal: contém os botões de navegação para os módulos (Agendamentos, Médicos, Pacientes, etc.)
- API Swagger (para testar os endpoints REST):

---

## 🖼️ Interface do Sistema

<img width="751" height="660" alt="image" src="https://github.com/user-attachments/assets/3ec3fff7-9f38-49f4-ae42-d4a04547dd93" />

---

## ⚙️ Tecnologias Utilizadas

### 🔹 Backend
- [Java 17+](https://www.oracle.com/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
  - Spring Web (REST Controllers)
  - Spring Data JPA (persistência)
  - Validação com `javax.validation` / `jakarta.validation`
- [Hibernate](https://hibernate.org/)
- Banco de dados:
  - **H2** (desenvolvimento)
  - Pode ser facilmente adaptado para MySQL ou PostgreSQL
- [Swagger / OpenAPI 3](https://swagger.io/) (documentação da API)
- Gerenciador de dependências: **Maven**

### 🔹 Frontend
- HTML5, CSS3 e JavaScript puro
- Layout **Glassmorphism** responsivo
- Paginação e interações dinâmicas via `fetch`

---

## 📂 Estrutura do Projeto

AgendamentoMedico/
│
├── src/main/java/com/example/AgendamentoMedico/
│ ├── config/ # Configurações gerais (ex.: CORS)
│ ├── controllers/ # Endpoints REST (Agendas, Médicos, Pacientes, etc.)
│ ├── dtos/ # DTOs de Request e Response
│ ├── enums/ # Enums (StatusAgenda, TipoConsulta, etc.)
│ ├── exceptions/ # Tratamento centralizado de erros
│ ├── mappers/ # Conversão entre entidades e DTOs
│ ├── models/ # Entidades JPA (Paciente, Médico, Agenda, etc.)
│ ├── repositories/ # Interfaces JPA para persistência
│ └── services/ # Lógica de negócio
│
├── src/main/resources/
│ ├── application.properties # Configurações do Spring Boot
│ ├── static/ # Arquivos do frontend
│ │ ├── index.html # Página inicial com menu de navegação
│ │ ├── agenda.html # Tela de agendamentos
│ │ ├── medico.html # Tela de médicos
│ │ ├── paciente.html # Tela de pacientes
│ │ ├── especialidade.html # Tela de especialidades
│ │ ├── convenio.html # Tela de convênios
│ │ ├── css/ # Estilos (Glassmorphism e layouts)
│ │ └── js/ # Lógica de frontend
│
├── pom.xml # Configuração do Maven
└── README.md # Documentação do projeto

---

## 📡 Endpoints Principais

| Recurso          | Método | URL                           | Descrição                     |
|------------------|--------|--------------------------------|--------------------------------|
| Agendamentos     | GET    | `/agendas`                     | Lista todos os agendamentos   |
|                  | POST   | `/agendas/agendar`             | Cria um novo agendamento      |
|                  | PATCH  | `/agendas/{id}/cancelar`       | Cancela um agendamento        |
| Médicos          | GET    | `/api/medicos`                 | Lista médicos                 |
| Pacientes        | GET    | `/api/pacientes`               | Lista pacientes               |
| Convênios        | GET    | `/api/convenios`               | Lista convênios               |
| Especialidades   | GET    | `/api/especialidades`          | Lista especialidades          |

---

## 🖥️ Como Rodar o Projeto

### 🔹 Pré-requisitos
- Java 17+ instalado
- Maven instalado
- (Opcional) Docker, se for usar container

### 🔹 Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/AgendamentoMedico.git

2. Entre na pasta do projeto:
   ```bash
   cd AgendamentoMedico

3. Compile e execute:
   ```bash
    mvn spring-boot:run
   
4. Acesse a aplicação no navegador:
   ```bash
   http://localhost:8080/index.html

## 🛠️ Estrutura das Branches

- main → branch de produção / releases
- develop → branch principal de desenvolvimento

## 📄 Licença

Este projeto é open-source e pode ser usado para fins educacionais e acadêmicos.

---

## ✍️ Autores:
Desenvolvido por Caio Victor, Josiane Cavalheiro, Marcilio Silva e Valdeci Alcântara 🚀
Com colaboração para layout, validações e estrutura de branches.

