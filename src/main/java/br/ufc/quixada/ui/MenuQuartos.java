package br.ufc.quixada.ui;

import br.ufc.quixada.dao.QuartoDAO;
import br.ufc.quixada.entity.Quarto;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuQuartos {

  @Autowired
  private QuartoDAO baseQuartos;

  // Menu principal para interação com quartos
  private static final String MENU =
    "Menu Quartos\n" +
    "1 - Inserir\n" +
    "2 - Atualizar por ID\n" +
    "3 - Remover por ID\n" +
    "4 - Exibir por ID\n" +
    "5 - Exibir todos\n" +
    "6 - Exibir todos por tipo\n" +
    "7 - Exibir todos disponiveis\n" + 
    "8 - Menu anterior";

  // Método para obter informações de um quarto
  public void obterQuarto(Quarto quarto) {
    quarto.setCapacidade(
      JOptionPane.showInputDialog("Capacidade", quarto.getCapacidade())
    );
    quarto.setDescricao(
      JOptionPane.showInputDialog("Descrição", quarto.getDescricao())
    );
    List<String> opcoesTipoQuarto = Arrays.asList(
      "Standard",
      "Master",
      "Deluxe"
    );

    // Use a lista no JOptionPane para permitir que o usuário escolha
    String tipoQuartoSelecionado = (String) JOptionPane.showInputDialog(
      null,
      "Selecione o tipo de quarto",
      "Tipo de Quarto",
      JOptionPane.PLAIN_MESSAGE,
      null,
      opcoesTipoQuarto.toArray(),
      quarto.getTipoDeQuarto()
    );

    // Atualize o tipo de quarto com a opção escolhida
    quarto.setTipoDeQuarto(tipoQuartoSelecionado);

    // Validação de entrada para o preço da diária
    boolean inputValido = false;
    do {
      try {
        String precoStr = JOptionPane.showInputDialog(
          "Valor da diária",
          quarto.getPrecoDaDiaria()
        );
        quarto.setPrecoDaDiaria(Float.parseFloat(precoStr));
        inputValido = true;
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
          null,
          "Erro: Digite um valor numérico válido para o preço da diária."
        );
      }
    } while (!inputValido);

    // Validação de entrada para disponibilidade
    List<Boolean> opcoesDisponibilidade = Arrays.asList(true, false);

    // Use a lista no JOptionPane para permitir que o usuário escolha
    boolean disponibilidadeSelecionada = (boolean) JOptionPane.showInputDialog(
      null,
      "Selecione a disponibilidade",
      "Disponibilidade do Quarto",
      JOptionPane.PLAIN_MESSAGE,
      null,
      opcoesDisponibilidade.toArray(),
      quarto.isDisponivel()
    );

    // Atualize a disponibilidade do quarto com a opção escolhida
    quarto.setDisponivel(disponibilidadeSelecionada);
  }

  // Método para exibir uma lista de quartos
  public void listaQuartos(List<Quarto> quartos) {
    StringBuilder listagem = new StringBuilder();
    for (Quarto quarto : quartos) {
      listagem.append(quarto).append("\n");
    }
    JOptionPane.showMessageDialog(
      null,
      listagem.length() == 0 ? "Nenhum quarto encontrado" : listagem
    );
  }

  // Método para exibir as informações de um quarto
  public void listaQuarto(Quarto quarto) {
    JOptionPane.showMessageDialog(
      null,
      quarto == null ? "Nenhum quarto encontrado" : quarto
    );
  }

  // Método principal para interação com quartos
  public void menu() {
    char opcao = '0';
    do {
      try {
        // Exibe o menu principal e obtém a opção escolhida pelo usuário
        opcao = JOptionPane.showInputDialog(MENU).charAt(0);
        switch (opcao) {
          case '1': // Inserir
            Quarto quarto = new Quarto();
            obterQuarto(quarto);
            baseQuartos.save(quarto);
            JOptionPane.showMessageDialog(
              null,
              "Quarto adicionado com sucesso!!"
            );
            break;
          case '2': // Atualizar por id
            int id = Integer.valueOf(
              JOptionPane.showInputDialog(
                "Digite o ID do quarto a ser alterado"
              )
            );
            Quarto quartoToUpdate = baseQuartos.findById(id).orElse(null);
            if (quartoToUpdate != null) {
              obterQuarto(quartoToUpdate);
              baseQuartos.save(quartoToUpdate);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrado quarto com o ID " + id
              );
            }
            break;
          case '3': // Remover por id
            int idToRemove = Integer.valueOf(
              JOptionPane.showInputDialog(
                "Digite o ID do quarto a ser removido"
              )
            );
            Quarto quartoToRemove = baseQuartos
              .findById(idToRemove)
              .orElse(null);
            if (quartoToRemove != null) {
              // Verifica se o quarto está associado a alguma reserva
              if (quartoToRemove.temReserva()) {
                JOptionPane.showMessageDialog(
                  null,
                  "Não é possível remover o quarto porque está associado a uma reserva."
                );
              } else {
                // Liberar o quarto
                quartoToRemove.setDisponivel(true);
                baseQuartos.save(quartoToRemove);

                // Remover o quarto
                baseQuartos.deleteById(quartoToRemove.getId());
                JOptionPane.showMessageDialog(
                  null,
                  "Quarto removido com sucesso!"
                );
              }
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrado quarto com o ID " + idToRemove
              );
            }
            break;
          case '4': // Exibir por id
            int idToDisplay = Integer.parseInt(
              JOptionPane.showInputDialog("Digite o ID do quarto a ser exibido")
            );
            Quarto quartoToDisplay = baseQuartos
              .findById(idToDisplay)
              .orElse(null);
            if (quartoToDisplay != null) {
              listaQuarto(quartoToDisplay);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Não foi encontrado quarto com o ID " + idToDisplay
              );
            }
            break;
          case '5': // Exibir todos
            listaQuartos(baseQuartos.findAll());
            break;
          case '6': // Exibir todos que contêm determinado tipo
            // Tipos de quarto disponíveis
            String[] tiposDisponiveis = { "Standard", "Master", "Deluxe" };

            // Exibe um diálogo de escolha para o usuário
            String tipoEscolhido = (String) JOptionPane.showInputDialog(
              null,
              "Escolha o tipo de quarto:",
              "Tipos de Quarto",
              JOptionPane.PLAIN_MESSAGE,
              null,
              tiposDisponiveis,
              tiposDisponiveis[0] // Valor padrão (pode ser null ou outro valor padrão)
            );

            // Verifica se o usuário selecionou um tipo
            if (tipoEscolhido != null) {
              //o método findByTipoDeQuartoContainingIgnoreCase para obter a lista de quartos
              List<Quarto> quartosPorTipo = baseQuartos.findByTipoDeQuartoContainingIgnoreCase(
                tipoEscolhido
              );

              // Verifica se a lista não está vazia antes de exibir
              if (!quartosPorTipo.isEmpty()) {
                listaQuartos(quartosPorTipo);
              } else {
                JOptionPane.showMessageDialog(
                  null,
                  "Nenhum quarto encontrado com o tipo: " + tipoEscolhido,
                  "Aviso",
                  JOptionPane.INFORMATION_MESSAGE
                );
              }
            }
            break;
          case '7': // Exibir todos disponíveis
            List<Quarto> quartosDisponiveis = baseQuartos.findByDisponivel(
              true
            );
            if (!quartosDisponiveis.isEmpty()) {
              listaQuartos(quartosDisponiveis);
            } else {
              JOptionPane.showMessageDialog(
                null,
                "Nenhum quarto disponível encontrado",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE
              );
            }
            break;
          case '8': // Sair
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opção Inválida");
            break;
        }
      } catch (NumberFormatException e) {
        // Trata erro ao processar entrada numérica
        log.error("Erro ao processar entrada numérica: {}", e.getMessage(), e);
        JOptionPane.showMessageDialog(
          null,
          "Erro: Entrada inválida. Digite um número válido."
        );
      } catch (Exception e) {
        // Trata erro durante a execução
        log.error("Erro durante a execução: {}", e.getMessage(), e);
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
      }
    } while (opcao != '8'); // Loop continua até o usuário escolher sair
  }
}
