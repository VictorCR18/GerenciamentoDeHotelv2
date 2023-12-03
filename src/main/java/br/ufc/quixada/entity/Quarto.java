package br.ufc.quixada.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "quartos")
@Table(name = "quartos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedQueries(
  {
    @NamedQuery(
      name = "findByTipo",
      query = "select q from Quarto q where q.tipo = :tipo"
    ),
  }
)
public class Quarto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(unique = true, nullable = false)
  private String numero;

  private String tipo;
  private float precoDaDiaria;
  private boolean disponivel;
  private String capacidade;

  @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL)
  private List<Reserva> reservas;

  @Override
  public String toString() {
    return (
      "QuartoId: " +
      id +
      ", " +
      "Numero: " +
      numero +
      ", " +
      "Capacidade: " +
      capacidade +
      ", " +
      "Tipo: " +
      tipo +
      ", " +
      "Preço da Diaria: R$" +
      precoDaDiaria +
      ", " +
      "Disponivel: " +
      disponivel
    );
  }

  public boolean temReserva() {
    return reservas != null && !reservas.isEmpty();
  }
}
