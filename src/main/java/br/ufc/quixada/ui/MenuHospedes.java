package br.ufc.quixada.ui;

import br.ufc.quixada.dao.HospedeDAO;
import br.ufc.quixada.entity.Hospede;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuHospedes {

  @Autowired
  private HospedeDAO baseHospedes;

  // Método para obter informações do hóspede
  public void obterHospede(Hospede h) {
    String nome = JOptionPane.showInputDialog("Nome", h.getNome());
    String cpf = JOptionPane.showInputDialog("CPF", h.getCpf());
    String fone = JOptionPane.showInputDialog("Fone", h.getFone());
    h.setNome(nome);
    h.setCpf(cpf);
    h.setFone(fone);
  }

  // Método para exibir a lista de hóspedes
  @Transactional
  public void listaHospedes(List<Hospede> hospedes) {
    StringBuilder listagem = new StringBuilder();
    for (Hospede h : hospedes) {
      listagem.append(h.toStringCompleto()).append("\n");
    }
    JOptionPane.showMessageDialog(
      null,
      listagem.length() == 0 ? "Nenhum hospede encontrado" : listagem
    );
  }

  // Método para exibir informações de um hóspede
  @Transactional
  public void listaHospede(Hospede h) {
    JOptionPane.showMessageDialog(
      null,
      h == null ? "Nenhum cliente encontrado" : h.toStringCompleto()
    );
  }

  // Método para obter a lista de hóspedes ordenados por nome
  public List<Hospede> listaHospedesOrdenadosPorNome() {
    return baseHospedes.findAllByOrderByNome();
  }

  // Método principal que exibe o menu para interação com hóspedes
  @Transactional
  public void menu() {
    StringBuilder menu = new StringBuilder("Menu Hospedes\n")
      .append("1 - Inserir\n")
      .append("2 - Atualizar por CPF\n")
      .append("3 - Remover por CPF\n")
      .append("4 - Exibir por CPF\n")
      .append("5 - Exibir por ID\n")
      .append("6 - Exibir todos\n")
      .append("7 - Exibir todos que contém determinado nome\n")
      .append("8 - Exibir por ordem alfabética\n")
      .append("9 - Menu anterior");
    char opcao = '0';
    do {
      try {
        Hospede h;
        String cpf;
        opcao = JOptionPane.showInputDialog(menu).charAt(0);
        switch (opcao) {
          case '1': // Inserir
            h = new Hospede();
            obterHospede(h);
            h.setId(UUID.randomUUID().toString());
            baseHospedes.save(h);
            JOptionPane.showMessageDialog(
              null,
              "Hóspede adicionado com sucesso!!"
            );
            break;
          case '2': // Atualizar por CPF
            cpf =
              JOptionPane.showInputDialog(
                "Digite o CPF do hospede a ser alterado"
              );
            h = baseHospedes.findFirstByCpf(cpf);
            if (h != null) {
              obterHospede(h);
              baseHospedes.save(h);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi possível atualizar, pois o hospede não foi encontrado."
              );
            }
            break;
          case '3': // Remover por CPF
            String cpfToRemove = JOptionPane.showInputDialog(
              "Digite o CPF do Hospede a ser removido"
            );
            Hospede hospedeToRemove = baseHospedes
              .findByCpf(cpfToRemove)
              .orElse(null);

            if (hospedeToRemove != null) {
              // Verifica se o hospede está associado a alguma reserva
              if (hospedeToRemove.getReservas() != null && !hospedeToRemove.getReservas().isEmpty()) {
                JOptionPane.showMessageDialog(
                  null,
                  "Não é possível remover o hospede porque está associado a uma reserva."
                );
              } else {
                // Remove o hospede apenas se não houver reserva associada
                baseHospedes.deleteByCpf(hospedeToRemove.getCpf());
                JOptionPane.showMessageDialog(
                  null,
                  "Hospede removido com sucesso!"
                );
              }
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrado hospede com o CPF " + cpfToRemove
              );
            }
            break;
          case '4': // Exibir por CPF
            cpf = JOptionPane.showInputDialog("CPF");
            h = baseHospedes.buscaPorCpfViaConsultaNomeada(cpf);
            listaHospede(h);
            break;
          case '5': // Exibir por id
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID"));
            h = baseHospedes.findById(id).orElse(null);
            listaHospede(h);
            break;
          case '6': // Exibir todos
            listaHospedes(baseHospedes.findAll());
            break;
          case '7': // Exibir todos que contem determinado nome
            String nome = JOptionPane.showInputDialog("Nome");
            listaHospedes(baseHospedes.buscaPorNomeContendoString(nome));
            break;
          case '8': // Exibir todos por ordem alfabética
            listaHospedes(listaHospedesOrdenadosPorNome());
            break;
          case '9': // Sair
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opção Inválida");
            break;
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
      }
    } while (opcao != '9');
  }
}
