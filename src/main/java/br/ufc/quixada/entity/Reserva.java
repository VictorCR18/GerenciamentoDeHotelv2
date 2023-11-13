package br.ufc.quixada.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.*;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Reserva {

  // Formato para formatar a data e hora
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
    "dd/MM/yyyy HH:mm:ss"
  );

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  // Relacionamento muitos-para-um com Hospede
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "hospede_id")
  private Hospede hospede;

  // Relacionamento muitos-para-um com Quarto
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "quarto_id")
  private Quarto quarto;

  // Data e hora da reserva (obrigatória)
  @NonNull
  private LocalDateTime dataHora;

  // Método para obter o valor total da reserva com base no preço diário do quarto
  public float getValorTotal() {
    // Assumindo que o valor do quarto está diretamente associado à reserva
    return quarto != null ? quarto.getPrecoDaDiaria() : 0;
  }

  // Método toString para exibir informações da reserva
  @Override
  public String toString() {
    // Obtém o nome do hóspede (ou "N/A" se não houver hóspede associado)
    String hospedeNome = hospede != null ? hospede.getNome() : "N/A";
    
    // Obtém o ID do quarto (ou "N/A" se não houver quarto associado)
    String quartoId = quarto != null ? quarto.getId().toString() : "N/A";

    // Retorna uma string formatada com informações da reserva
    return (
      "Reserva [id=" +
      id +
      ", hospede=" +
      hospedeNome +
      ", quarto=" +
      quartoId +
      ", dataHora=" +
      dataHora.format(formatter) +
      "]"
    );
  }
}
