package br.ufc.quixada.ui;

import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.ufc.quixada")
@EntityScan("br.ufc.quixada.entity")
@EnableJpaRepositories("br.ufc.quixada.dao")
@Slf4j
public class MenuPrincipal implements CommandLineRunner {

  @Autowired
  private MenuHospedes menuHospedes;

  @Autowired
  private MenuQuartos menuQuartos;

  @Autowired
  private MenuReservas menuReservas;

  // Método principal para iniciar a aplicação
  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(
      MenuPrincipal.class
    );
    builder.headless(false).run(args);
  }

  // Implementação do método run da interface CommandLineRunner
  @Override
  public void run(String... args) throws Exception {
    StringBuilder menu = new StringBuilder("Menu Principal\n")
      .append("1 - Hospedes\n")
      .append("2 - Quartos\n")
      .append("3 - Reservas\n")
      .append("4 - Sair");
    char opcao = '0';
    do {
      try {
        // Exibe o menu principal e obtém a opção escolhida pelo usuário
        opcao = JOptionPane.showInputDialog(menu).charAt(0);
        switch (opcao) {
          case '1':
            // Chama o menu de interação com hóspedes
            menuHospedes.menu();
            break;
          case '2':
            // Chama o menu de interação com quartos
            menuQuartos.menu();
            break;
          case '3':
            // Chama o menu de interação com reservas
            menuReservas.menu();
            break;
          case '4':
            // Opção para sair do programa
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opção Inválida");
            break;
        }
      } catch (Exception e) {
        // Trata exceções durante a execução
        log.error("Erro durante a execução: {}", e.getMessage(), e);
        JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
      }
    } while (opcao != '4'); // Loop continua até o usuário escolher sair
  }
}
