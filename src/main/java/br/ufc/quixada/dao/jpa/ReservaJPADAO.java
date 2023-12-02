package br.ufc.quixada.dao.jpa;

import br.ufc.quixada.entity.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaJPADAO extends JpaRepository<Reserva, Integer> {

  // Consulta JPQL que retorna um objeto Reserva com associações Hospede
  @Query(
    "SELECT NEW br.ufc.quixada.entity.Reserva(" +
    "hospedes.nome AS nome, " +
    "quartos.id, " +
    "quartos.precoDaDiaria) " +
    "FROM Hospede hospedes " +
    "JOIN Reserva reservas ON hospedes.id = reservas.hospede.id " +
    "JOIN Quarto quartos ON reservas.quarto.id = quartos.id " +
    "WHERE quartos.precoDaDiaria >= :valor"
  )
  List<Reserva> findByValorTotalGreaterThanEqual(@Param("valor") float valor);
}
