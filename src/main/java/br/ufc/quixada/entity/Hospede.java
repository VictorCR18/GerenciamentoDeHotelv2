package br.ufc.quixada.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@NamedQueries(
  {
    @NamedQuery(
      name = "hospedePorCpf",
      query = "select h from Hospede h where h.cpf = :cpf"
    ),
  }
)
@Entity
@Table(name = "hospedes")
@Data
public class Hospede {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String cpf;

  private String nome;
  private String fone;

  @OneToMany(
    mappedBy = "hospede",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Reserva> reservas;

  @Override
  public String toString() {
    return "Hospede [id=" + id + ", cpf=" + cpf + ", nome=" + nome + "]";
  }

  public String toStringCompleto() {
    return (
      "Hospede [id=" +
      id +
      ", cpf=" +
      cpf +
      ", nome=" +
      nome +
      ", fone=" +
      fone +
      "]"
    );
  }

  // Método para adicionar reserva
  public void adicionarReserva(Reserva reserva) {
    if (reservas == null) {
      reservas = new ArrayList<>();
    }
    reservas.add(reserva);
    reserva.setHospede(this);
  }

  // Método para remover reserva
  public void removerReserva(Reserva reserva) {
    if (reservas != null) {
      reservas.remove(reserva);
      reserva.setHospede(null);
    }
  }

  // Verifica se o hóspede já está associado a uma reserva
  public boolean temReserva() {
    return reservas != null && !reservas.isEmpty();
  }
}
