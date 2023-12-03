package br.ufc.quixada.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservas")
@Entity
@Table(name = "reservas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedQueries(
  {
    @NamedQuery(
      name = "findReservasByHospedeNome",
      query = "select r from Reserva r where r.hospede.nome = :nome"
    ),
  }
)
public class Reserva {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "reserva_id")
  @Id
  private String id;

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
    "dd/MM/yyyy HH:mm:ss"
  );

  @NonNull
  private LocalDateTime dataHora;

  @DBRef
  @ManyToOne
  @JoinColumn(name = "quarto_id")
  @NonNull
  private Quarto quarto;

  @DBRef
  @ManyToOne
  @JoinColumn(name = "hospede_id")
  @NonNull
  private Hospede hospede;

  @Override
  public String toString() {
    String hospedeNome = hospede != null ? hospede.getNome() : "N/A";

    return (
      "Reserva [id=" +
      id +
      ", hospede=" +
      hospedeNome +
      ", quarto=" +
      (quarto != null ? quarto.getNumero() : "N/A") +
      ", dataHora=" +
      (dataHora != null ? dataHora.format(formatter) : "N/A") +
      "]"
    );
  }
}
