package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Hospede;
import br.ufc.quixada.entity.Reserva;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReservaDAO {
    void save(Reserva reserva);

    void deleteAllByHospede(Hospede hospede);

    void deleteById(String id);

    Optional<Reserva> findById(String id);

    public List<Reserva> findByHospedeId(String id);

    public List<Reserva> findReservasByHospedeNome(String nome);

    List<Reserva> findAll();

    List<Reserva> findByQuartoNumeroContaining(String numero);

    List<Reserva> findReservasByQuartoTipo(String tipo);

    List<Reserva> findByQuartoNumero(String numero);

    @Transactional
    void deleteByQuartoNumero(String numeroQuarto);
}
