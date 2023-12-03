package br.ufc.quixada.dao.mongo;

import br.ufc.quixada.dao.QuartoDAO;
import br.ufc.quixada.entity.Quarto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartoMongoDAO extends QuartoDAO, MongoRepository<Quarto, String> {

    // Consulta para encontrar quartos por tipo contendo uma determinada string (ignorando maiúsculas/minúsculas)
    List<Quarto> findByTipoContainingIgnoreCase(String str);

    // Consulta para encontrar quartos por disponibilidade
    List<Quarto> findByDisponivel(boolean disponivel);

    void deleteById(String id);
}
