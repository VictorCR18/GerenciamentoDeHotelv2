# Gerenciamento de Hotel

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
    -capacidade: String
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

  Hospede "1" --> "0..1" Reserva : possui
  Reserva "1" --> "1" Quarto
```