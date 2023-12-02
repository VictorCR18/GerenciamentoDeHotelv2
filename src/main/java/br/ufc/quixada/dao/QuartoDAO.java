package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Quarto;
import java.util.List;
import java.util.Optional;

public interface QuartoDAO {

    List<Quarto> findByTipoDeQuartoContainingIgnoreCase(String str);

    List<Quarto> findByDisponivel(boolean disponivel);

    void save(Quarto quarto);

    void deleteById(Integer id);

    Optional<Quarto> findById(Integer id);

    List<Quarto> findAll();
}
