package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Reserva;
import java.util.List;
import java.util.Optional;

public interface ReservaDAO {

    List<Reserva> findByValorTotalGreaterThanEqual(float valor);

    void save(Reserva reserva);

    void deleteById(Integer id);

    Optional<Reserva> findById(Integer id);

    List<Reserva> findAll();
}
