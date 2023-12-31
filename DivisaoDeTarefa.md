# Gerenciamento de Hotel

----------------------- Divisão de Tarefas ---------------------------------
Nauan
Responsável pela criação do Menu principal, Menu Reserva, ReservaDAO, entidade Reserva e também pela criação do diagrama de classes através da ferramenta Mermaid

Fez a configuração da application.properties, o DAO generico, refez as entidades e testes

Victor
Responsável pela entidade Hospede, Quartos, Menu de ambos, HospedeDAO, QuartosDAO e preenchimento do Banco de Dados.

Fez o Banco no Mongo Atlas, DAO do JPA e Mongo, refez os menus
----------------------- // ---------------------------------

Aplicação JPA com Spring Boot e Spring Data JPA.

A aplicação possui menus e sub-menus para cadastro de hospedes, quartos e reservas de quartos.

```mermaid
classDiagram

  class Hospede {
    -cpf: String
    -id: Integer
    -nome: String
    -fone: String
    -reservas: List<Reserva>

    +adicionarReserva(reserva: Reserva): void
    +removerReserva(reserva: Reserva): void
    +temReserva(): boolean
  }

  class Quarto {
    -id: Integer
    -Numero: String
    -descricao: String
    -tipoDeQuarto: String
    -precoDaDiaria: float
    -disponivel: boolean
    -reservas: List<Reserva>

    +temReserva(): boolean
  }

  class Reserva {
    -id: Integer
    -hospede: Hospede
    -quarto: Quarto
    -dataHora: LocalDateTime

    +getValorTotal(): float
  }

  Hospede -- Reserva : possui
  Reserva -- Quarto
```

