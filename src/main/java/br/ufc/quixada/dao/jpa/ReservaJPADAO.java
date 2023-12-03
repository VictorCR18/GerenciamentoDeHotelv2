package br.ufc.quixada.dao.jpa;

import br.ufc.quixada.dao.ReservaDAO;
import br.ufc.quixada.entity.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaJPADAO
  extends ReservaDAO, JpaRepository<Reserva, Integer> {
  @Query("SELECT r FROM Reserva r WHERE r.hospede.id = :id")
  List<Reserva> findByHospedeId(@Param("id") String id);

  @Query("SELECT r FROM Reserva r WHERE r.hospede.nome = :nome")
  List<Reserva> findReservasByHospedeNome(@Param("nome") String nome);

  @Query("SELECT r FROM Reserva r WHERE r.quarto.numero LIKE %:numero%")
  List<Reserva> findByQuartoNumeroContaining(@Param("numero") String numero);

  @Query("SELECT r FROM Reserva r WHERE r.quarto.tipo = :tipo")
  List<Reserva> findReservasByQuartoTipo(@Param("tipo") String tipo);

  @Query("SELECT r FROM Reserva r WHERE r.quarto.numero = :numero")
  List<Reserva> findByQuartoNumero(@Param("numero") String numero);
}
