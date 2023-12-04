package br.ufc.quixada.dao.jpa;

import br.ufc.quixada.dao.QuartoDAO;
import br.ufc.quixada.entity.Quarto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartoJPADAO extends QuartoDAO, JpaRepository<Quarto, Integer> {
  
  // Consulta JPQL para encontrar quartos por tipo contendo uma determinada string (ignorando maiúsculas/minúsculas)
  List<Quarto> findByTipoContainingIgnoreCase(String str);

  // Consulta JPQL para encontrar quartos por disponibilidade
  List<Quarto> findByDisponivelTrue();

  void deleteById(String id);
}
