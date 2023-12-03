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

@NamedQueries({
  @NamedQuery(name = "findReservasByHospedeNome", query = "select r from Reserva r where r.hospede.nome = :nome"),
})

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
    // // Obtém o nome do hóspede (ou "N/A" se não houver hóspede associado)
    // String hospedeNome = hospede != null ? hospede.getNome() : "N/A";

    // // Obtém o ID do quarto (ou "N/A" se não houver quarto associado)
    // String quartoId = quarto != null ? quarto.getId().toString() : "N/A";

    return (
      "Reserva [id=" +
      id +
      ", hospede=" +
      hospede.getNome() +
      ", quarto=" +
      quarto.getNumero() +
      ", dataHora=" +
      dataHora.format(formatter) +
      "]"
    );
  }
}
