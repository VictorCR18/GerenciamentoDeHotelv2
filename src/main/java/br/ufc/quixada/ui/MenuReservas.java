package br.ufc.quixada.ui;

import br.ufc.quixada.dao.HospedeDAO;
import br.ufc.quixada.dao.QuartoDAO;
import br.ufc.quixada.dao.ReservaDAO;
import br.ufc.quixada.entity.Hospede;
import br.ufc.quixada.entity.Quarto;
import br.ufc.quixada.entity.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuReservas {

  @Autowired
  private ReservaDAO baseReservas;

  @Autowired
  private QuartoDAO baseQuartos;

  @Autowired
  private HospedeDAO baseHospedes;

  public void obterReserva(Reserva reserva) {
    List<Quarto> quartosDisponiveis = baseQuartos.findByDisponivelTrue();
    
    if (quartosDisponiveis.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Nenhum quarto disponível.");
        return;
    }

    Quarto[] arrayQuartos = quartosDisponiveis.toArray(new Quarto[0]);
    Quarto quartoEscolhido = (Quarto) JOptionPane.showInputDialog(
        null,
        "Selecione um quarto",
        "Quartos",
        JOptionPane.PLAIN_MESSAGE,
        null,
        arrayQuartos,
        reserva.getHospede()
    );

    if (quartoEscolhido != null && quartoEscolhido.isDisponivel()) {
        reserva.setQuarto(quartoEscolhido);
        reserva.setDataHora(LocalDateTime.now());
    } else {
        JOptionPane.showMessageDialog(
            null,
            "Nenhum quarto disponível ou quarto escolhido não está mais disponível. A reserva não será salva."
        );
        // Se o quarto não estiver disponível, retorne sem salvar a reserva
        return;
    }
}

  public void listarReservas(List<Reserva> reservas) {
    StringBuilder listagem = new StringBuilder();
    for (Reserva r : reservas) {
      listagem.append(r.toString()).append("\n");
    }
    JOptionPane.showMessageDialog(
      null,
      listagem.length() == 0
        ? "Nenhuma reserva encontrada"
        : listagem.toString()
    );
  }

  private void cadastrarReserva() {
    try {
      List<Hospede> hospedes = baseHospedes.findAll();
      Hospede hospedeSelecionado = (Hospede) JOptionPane.showInputDialog(
        null,
        "Selecione um hóspede",
        "Hóspedes",
        JOptionPane.PLAIN_MESSAGE,
        null,
        hospedes.toArray(),
        null
      );

      if (hospedeSelecionado == null) {
        JOptionPane.showMessageDialog(
          null,
          "Nenhum hóspede selecionado. A reserva não será salva."
        );
        return;
      }

      Reserva reserva = new Reserva();
      reserva.setHospede(hospedeSelecionado);

      obterReserva(reserva);

      // Antes de salvar a reserva, altera a disponibilidade do quarto associado para falso
      Quarto quartoAssociado = reserva.getQuarto();
      if (quartoAssociado != null) {
        quartoAssociado.setDisponivel(false);
        baseQuartos.save(quartoAssociado); // Atualiza o estado do quarto no banco de dados
      }

      reserva.setId(UUID.randomUUID().toString());
      baseReservas.save(reserva);
      JOptionPane.showMessageDialog(null, "Reserva realizada com sucesso!");
    } catch (Exception e) {
      log.error("Erro ao salvar reserva: {}", e.getMessage(), e);
      JOptionPane.showMessageDialog(
        null,
        "Erro ao salvar reserva: " + e.getMessage()
      );
    }
  }

  public void menu() {
    char opcao = '0';
    do {
      try {
        StringBuilder menu = new StringBuilder("Menu de reservas")
          .append("\n")
          .append("1 - Inserir Reserva\n")
          .append("2 - Atualizar Reserva por ID\n")
          .append("3 - Deletar Reserva por ID\n")
          .append("4 - Listar todas as reservas\n")
          .append("5 - Menu anterior\n");
        String id;
        opcao = JOptionPane.showInputDialog(menu).charAt(0);
        switch (opcao) {
          case '1':
            cadastrarReserva();
            break;
          case '2':
            id =
              JOptionPane.showInputDialog(
                "Digite o ID da reseva a ser alterado"
              );
            Reserva reservaToUpdate = baseReservas.findById(id).orElse(null);
            if (reservaToUpdate != null) {
              obterReserva(reservaToUpdate);
              baseReservas.save(reservaToUpdate);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrado reserva com o ID " + id
              );
            }
            break;
            case '3':
            String idToRemove = JOptionPane.showInputDialog(
                "Digite o ID da reserva a ser removido"
            );
            Reserva reservaToRemove = baseReservas.findById(idToRemove).orElse(null);
        
            if (reservaToRemove != null) {
                // Remove a reserva apenas se não houver reserva associada
                baseReservas.delete(reservaToRemove);
                JOptionPane.showMessageDialog(
                    null,
                    "Reserva removida com sucesso!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Não foi encontrada reserva com o ID " + idToRemove
                );
            }
            break;
          case '4':
            listarReservas(baseReservas.findAll());
            break;
          case '5':
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opção inválida");
            break;
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
      }
    } while (opcao != '5');
  }
}
