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
      .append("2 - Atualizar por id\n")
      .append("3 - Remover por id\n") 
      .append("4 - Exibir por id\n")
      .append("5 - Exibir todos\n")
      .append("6 - Menu anterior");
    char opcao = '0';
    do {
      try {
        Reserva reserva;
        String id;
        opcao = JOptionPane.showInputDialog(menu).charAt(0);
        switch (opcao) {
          case '1': // Inserir
            reserva = new Reserva();
            obterESalvarReserva(reserva);
            break;
          case '2': // Atualizar por id
            id =
              JOptionPane.showInputDialog(
                "Digite o ID da reserva a ser alterada"
              );
            reserva = baseReservas.findById(id).orElse(null);
            if (reserva != null) {
              obterESalvarReserva(reserva);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrada compra com o ID " + id
              );
            }
            break;
            case '3': // Remover por id
            id = JOptionPane.showInputDialog("Digite o ID da reserva a ser removida");
            reserva = baseReservas.findById(id).orElse(null);
        
            if (reserva != null) {
                // Obtém o quarto associado à reserva
                Quarto quartoDaReserva = reserva.getQuarto();
        
                // Remove a reserva
                baseReservas.deleteById(reserva.getId());
        
                // Atualiza o quarto para disponível
                quartoDaReserva.setDisponivel(true);
                baseQuartos.save(quartoDaReserva);
        
                JOptionPane.showMessageDialog(null, "Reserva removida com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi encontrada reserva com o ID " + id);
            }
            break;
        
          case '4': // Exibir por id
            id =
              JOptionPane.showInputDialog(
                "Digite o ID da reserva a ser exibida"
              );
            reserva = baseReservas.findById(id).orElse(null);
            if (reserva != null) {
              listaReserva(reserva);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrada compra com o ID " + id
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
