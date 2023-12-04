package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Quarto;
import java.util.List;
import java.util.Optional;

public interface QuartoDAO {

    List<Quarto> findByTipoContainingIgnoreCase(String str);

    List<Quarto> findByDisponivelTrue();

    void save(Quarto quarto);

    void deleteById(String id);

    Optional<Quarto> findById(String idToDisplay);

    List<Quarto> findAll();
}
