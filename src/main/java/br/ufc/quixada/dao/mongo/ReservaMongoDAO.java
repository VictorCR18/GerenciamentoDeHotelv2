package br.ufc.quixada.dao.mongo;

import br.ufc.quixada.dao.ReservaDAO;
import br.ufc.quixada.entity.Hospede;
import br.ufc.quixada.entity.Reserva;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaMongoDAO extends ReservaDAO, MongoRepository<Reserva, String> {
    @Query("{ 'hospede.id' : ?0 }")
    List<Reserva> findByHospedeId(String id);

    @Query("{ 'hospede.nome' : ?0 }")
    List<Reserva> findReservasByHospedeNome(String nome);

    @Query("{'quarto.numero': { $regex: ?0, $options: 'i' }}")
    List<Reserva> findByQuartoNumeroContaining(String numero);

    @Query("{'quarto.tipo' : ?0 }")
    List<Reserva> findReservasByQuartoTipo(String tipo);  

    @Query("{'quarto.numero': ?0}")
    List<Reserva> findByQuartoNumero(String numero);

    void deleteAllByHospede(Hospede hospede);

    @Transactional
    void deleteByQuartoNumero(String numeroQuarto);
}
