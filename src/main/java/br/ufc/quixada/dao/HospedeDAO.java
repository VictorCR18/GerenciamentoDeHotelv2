package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Hospede;
import java.util.List;
import java.util.Optional;

public interface HospedeDAO {

    Hospede findFirstByCpf(String cpf);

    Hospede buscaPrimeiroPorCpf(String cpf);

    List<Hospede> findByNomeStartingWithIgnoreCase(String str);

    List<Hospede> buscaPorNomeContendoString(String nome);

    long count();

    void save(Hospede hospede);

    void deleteById(Integer id);

    Optional<Hospede> findById(Integer id);

    List<Hospede> findAll();

    Hospede findHospedeWithReservasByCpf(String cpf);

    Hospede buscaPorCpfViaConsultaNomeada(String cpf);
}

