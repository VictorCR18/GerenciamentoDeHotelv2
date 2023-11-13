package br.ufc.quixada.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "quartos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Quarto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NonNull
  private String capacidade;
  private String descricao;
  private String tipoDeQuarto;
  private float precoDaDiaria;
  private boolean disponivel;

  @OneToMany(mappedBy = "quarto", fetch = FetchType.EAGER)
  private List<Reserva> reservas;

  // Método que verifica se o quarto possui reservas
  public boolean temReserva() {
      return reservas != null && !reservas.isEmpty();
  }
}
