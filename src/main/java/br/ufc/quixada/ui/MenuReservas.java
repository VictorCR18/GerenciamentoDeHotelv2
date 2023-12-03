package br.ufc.quixada.ui;

import br.ufc.quixada.dao.HospedeDAO;
import br.ufc.quixada.dao.QuartoDAO;
import br.ufc.quixada.dao.ReservaDAO;
import br.ufc.quixada.entity.Hospede;
import br.ufc.quixada.entity.Quarto;
import br.ufc.quixada.entity.Reserva;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuReservas {

  @Autowired
  private ReservaDAO baseReservas;

  @Autowired
  private HospedeDAO baseHospedes;

  @Autowired
  private QuartoDAO baseQuartos;

  // Método para obter e salvar uma reserva
  public void obterESalvarReserva(Reserva reserva) {
    try {
      // Obter lista de hóspedes
      List<Hospede> hospedes = baseHospedes.findAll();
      Hospede hospedeSelecionado = (Hospede) JOptionPane.showInputDialog(
        null,
        "Selecione um hóspede",
        "Hóspedes",
        JOptionPane.PLAIN_MESSAGE,
        null,
        hospedes.toArray(),
        reserva.getHospede()
      );

      if (hospedeSelecionado == null) {
        JOptionPane.showMessageDialog(
          null,
          "Nenhum hóspede selecionado. A reserva não será salva."
        );
        return;
      }

      reserva.setHospede(hospedeSelecionado);

      // Obter lista de quartos disponíveis
      List<Quarto> quartosDisponiveis = baseQuartos.findByDisponivel(true);
      Quarto quartoSelecionado = (Quarto) JOptionPane.showInputDialog(
        null,
        "Selecione um quarto",
        "Quartos Disponíveis",
        JOptionPane.PLAIN_MESSAGE,
        null,
        quartosDisponiveis.toArray(),
        reserva.getQuarto()
      );

      if (quartoSelecionado != null) {
        reserva.setQuarto(quartoSelecionado);

        if (reserva.getDataHora() == null) {
          reserva.setDataHora(LocalDateTime.now());
        }

        // Iniciar transação para garantir consistência nos dados
        baseReservas.save(reserva);

        // Atualizar a disponibilidade do quarto para "falso"
        quartoSelecionado.setDisponivel(false);
        baseQuartos.save(quartoSelecionado);

        Hibernate.initialize(hospedeSelecionado.getReservas());

        // Inicializar a lista de reservas do hóspede se for null
        List<Reserva> reservasDoHospede = hospedeSelecionado.getReservas();
        if (reservasDoHospede == null) {
          reservasDoHospede = new ArrayList<>();
        }

        // Associar a reserva ao hóspede
        reservasDoHospede.add(reserva);
        hospedeSelecionado.setReservas(reservasDoHospede);
        baseHospedes.save(hospedeSelecionado);

        JOptionPane.showMessageDialog(null, "Reserva realizada com sucesso!");
      } else {
        JOptionPane.showMessageDialog(
          null,
          "Nenhum quarto selecionado. A reserva não será salva."
        );
      }
    } catch (Exception e) {
      log.error("Erro ao salvar reserva: {}", e.getMessage(), e);
      JOptionPane.showMessageDialog(
        null,
        "Erro ao salvar reserva: " + e.getMessage()
      );
    }
  }

  // Método para exibir uma lista de reservas
  public void listaReservas(List<Reserva> reservas) {
    StringBuilder listagem = new StringBuilder();
    for (Reserva reserva : reservas) {
      listagem.append(reserva);
      listagem.append("\n");
    }
    JOptionPane.showMessageDialog(
      null,
      listagem.length() == 0 ? "Nenhuma reserva encontrada" : listagem
    );
  }

  // Método para exibir as informações de uma reserva
  public static void listaReserva(Reserva reserva) {
    JOptionPane.showMessageDialog(
      null,
      reserva == null ? "Nenhuma reserva encontrada" : reserva
    );
  }

  // Método principal para interação com reservas
  @Transactional
  public void menu() {
    StringBuilder menu = new StringBuilder("Menu Reservas\n")
      .append("1 - Inserir\n")
      .append("2 - Atualizar por N° do quarto\n")
      .append("3 - Remover por N° do quarto\n")
      .append("4 - Exibir por N° do quarto\n")
      .append("5 - Exibir todos\n")
      .append("6 - Menu anterior");
    char opcao = '0';
    do {
      try {
        Reserva reserva;
        opcao = JOptionPane.showInputDialog(menu).charAt(0);
        switch (opcao) {
          case '1': // Inserir
            reserva = new Reserva();
            obterESalvarReserva(reserva);
            break;
          case '2': // Atualizar por número do quarto
            String numeroQuartoAtualizar = JOptionPane.showInputDialog(
              "Digite o N° do quarto da reserva a ser alterada"
            );

            // Modificação na consulta para encontrar a reserva pelo número do quarto
            List<Reserva> reservasDoQuartoAtualizar = baseReservas.findByQuartoNumero(
              numeroQuartoAtualizar
            );

            if (!reservasDoQuartoAtualizar.isEmpty()) {
              // Vamos assumir que você quer atualizar a primeira reserva da lista.
              // Você pode ajustar conforme necessário, por exemplo, pedindo ao usuário para escolher entre as reservas.
              Reserva reservaAAtualizar = reservasDoQuartoAtualizar.get(0);

              // Atualiza a reserva com base nas informações fornecidas pelo usuário
              obterESalvarReserva(reservaAAtualizar);

              JOptionPane.showMessageDialog(
                null,
                "Reserva atualizada com sucesso!"
              );
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrada reserva para o quarto com o número " +
                numeroQuartoAtualizar
              );
            }
            break;
          case '3': // Remover por numero
            String numeroQuarto = JOptionPane.showInputDialog(
              "Digite o N° do quarto da reserva a ser removida"
            );

            // Modificação na consulta para encontrar a reserva pelo número do quarto
            List<Reserva> reservasDoQuarto = baseReservas.findByQuartoNumero(
              numeroQuarto
            );

            if (!reservasDoQuarto.isEmpty()) {
              // Vamos assumir que você quer remover a primeira reserva da lista.
              // Você pode ajustar conforme necessário, por exemplo, pedindo ao usuário para escolher entre as reservas.
              Reserva reservaARemover = reservasDoQuarto.get(0);

              // Obtém o quarto associado à reserva
              Quarto quartoDaReserva = reservaARemover.getQuarto();

              // Usando o método personalizado para excluir a reserva pelo número do quarto
              baseReservas.deleteByQuartoNumero(numeroQuarto);

              // Atualiza o quarto para disponível
              quartoDaReserva.setDisponivel(true);
              baseQuartos.save(quartoDaReserva);

              JOptionPane.showMessageDialog(
                null,
                "Reserva removida com sucesso!"
              );
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrada reserva para o quarto com o número " +
                numeroQuarto
              );
            }
            break;
          case '4': // Exibir por número do quarto
            String numeroQuartoExibir = JOptionPane.showInputDialog(
              "Digite o N° do quarto da reserva a ser exibida"
            );

            // Modificação na consulta para encontrar a reserva pelo número do quarto
            List<Reserva> reservasDoQuartoExibir = baseReservas.findByQuartoNumero(
              numeroQuartoExibir
            );

            if (!reservasDoQuartoExibir.isEmpty()) {
              // Vamos assumir que você quer exibir a primeira reserva da lista.
              // Você pode ajustar conforme necessário, por exemplo, pedindo ao usuário para escolher entre as reservas.
              Reserva reservaAExibir = reservasDoQuartoExibir.get(0);

              // Exibe as informações da reserva
              listaReserva(reservaAExibir);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrada reserva para o quarto com o número " +
                numeroQuartoExibir
              );
            }
            break;
          case '5': // Exibir todos
            listaReservas(baseReservas.findAll());
            break;
          case '6': // Menu anterior
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opção Inválida");
            break;
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
      }
    } while (opcao != '6');
  }
}
