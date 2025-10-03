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

## 📘 Casos de Uso

1. **Agendar consulta**
   - Usuário acessa a tela de agendamentos e seleciona um paciente cadastrado.
   - Define o médico, data, horário e especialidade desejada.
   - Sistema valida conflitos de horário e confirma o agendamento.

2. **Remarcar consulta**
   - Usuário busca um agendamento existente na listagem.
   - Altera data e horário conforme disponibilidade do profissional.
   - Sistema atualiza o registro e notifica o sucesso da operação.

3. **Cancelar consulta**
   - Usuário seleciona o agendamento desejado e solicita cancelamento.
   - Sistema registra o status como cancelado e libera o horário na agenda.

4. **Cadastrar novo paciente**
   - Usuário abre a tela de pacientes e preenche dados pessoais e convênio.
   - Sistema valida CPF, e-mail e demais informações obrigatórias.
   - Em caso de sucesso, o paciente fica disponível para novos agendamentos.

5. **Gerenciar médicos e especialidades**
   - Usuário registra novos profissionais vinculando suas especialidades.
   - Pode editar ou inativar médicos conforme necessidade da clínica.
   - Especialidades podem ser criadas, atualizadas ou removidas para manter a base alinhada à oferta de serviços.

6. **Administrar convênios**
   - Usuário cadastra convênios aceitos e define dados de contato e cobertura.
   - Sistema permite edição e exclusão para manter a lista sempre atualizada.
   - Pacientes podem ser associados aos convênios disponíveis durante o cadastro ou edição.

Esses casos de uso contemplam as principais jornadas do sistema, garantindo o gerenciamento completo do ciclo de vida das consultas médicas.

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


```plaintext
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
```
---

## 📡 Endpoints Principais

| Recurso          | Método  | URL                                        | Descrição                                 |
|------------------|---------|--------------------------------------------|-------------------------------------------|
| **Agendamentos** | GET     | `/agendas`                                 | Lista todos os agendamentos              |
|                  | GET     | `/agendas/{id}`                            | Busca um agendamento por ID              |
|                  | POST    | `/agendas/agendar`                         | Cria um novo agendamento                 |
|                  | PUT     | `/agendas/{id}/remarcar`                   | Remarca um agendamento existente         |
|                  | PATCH   | `/agendas/{id}/cancelar`                   | Cancela um agendamento                   |
|                  | DELETE  | `/agendas/{id}`                            | Remove um agendamento                    |
| **Médicos**      | GET     | `/api/medicos`                              | Lista todos os médicos (com paginação)   |
|                  | GET     | `/api/medicos/{id}`                         | Busca um médico por ID                   |
|                  | GET     | `/api/medicos/especialidade/{especialidade}`| Lista médicos por especialidade          |
|                  | POST    | `/api/medicos`                              | Cadastra um novo médico                  |
|                  | PUT     | `/api/medicos/{id}`                         | Atualiza um médico existente             |
|                  | DELETE  | `/api/medicos/{id}`                         | Remove um médico                          |
| **Pacientes**    | GET     | `/api/pacientes`                            | Lista todos os pacientes                 |
|                  | GET     | `/api/pacientes/{id}`                       | Busca um paciente por ID                 |
|                  | GET     | `/api/pacientes/email/{email}`              | Busca um paciente por email              |
|                  | GET     | `/api/pacientes/search?nome={nome}`         | Busca pacientes por nome                 |
|                  | POST    | `/api/pacientes`                            | Cadastra um novo paciente                |
|                  | PUT     | `/api/pacientes/{id}`                       | Atualiza um paciente existente           |
|                  | PATCH   | `/api/pacientes/{id}`                       | Atualiza parcialmente um paciente        |
|                  | DELETE  | `/api/pacientes/{id}`                       | Remove um paciente                        |
| **Convênios**    | GET     | `/api/convenios`                             | Lista todos os convênios                  |
|                  | GET     | `/api/convenios/{id}`                        | Busca um convênio por ID                  |
|                  | POST    | `/api/convenios`                             | Cadastra um novo convênio                 |
|                  | PUT     | `/api/convenios/{id}`                        | Atualiza um convênio existente            |
|                  | DELETE  | `/api/convenios/{id}`                        | Remove um convênio                         |
| **Especialidades** | GET   | `/api/especialidades`                        | Lista todas as especialidades             |
|                  | GET     | `/api/especialidades/{id}`                   | Busca uma especialidade por ID            |
|                  | POST    | `/api/especialidades`                        | Cadastra uma nova especialidade           |
|                  | PUT     | `/api/especialidades/{id}`                   | Atualiza uma especialidade existente      |
|                  | DELETE  | `/api/especialidades/{id}`                   | Remove uma especialidade                   |

---

## 🖥️ Como Rodar o Projeto

### 🔹 Pré-requisitos
- Java 17+ instalado
- Maven instalado
- (Opcional) Docker, se for usar container

### 🔹 Passos

### 🔹 Perfil de desenvolvimento com carga inicial
Caso precise carregar os dados iniciais fornecidos pelos scripts SQL, execute a aplicação com o perfil `dev` habilitado. Você pode fazer isso adicionando o parâmetro `--spring.profiles.active=dev` ao comando de execução (por exemplo, `mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev`).

O perfil padrão mantém `spring.sql.init.mode=embedded`, evitando a execução automática dos scripts em bancos de dados persistentes. Já o perfil `dev` reativa a carga inicial e adia a inicialização do JPA para garantir compatibilidade com o banco em memória H2.

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

### 🔹 Rodando com Docker

Caso prefira rodar a aplicação em um container Docker:

1. Buildar a imagem:
   ```bash
   docker build -t agendamento-medico .

2. Rodar o container:
   ```bash
   docker run -p 8080:8080 agendamento-medico

3. Acesse a aplicação no navegador:
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

