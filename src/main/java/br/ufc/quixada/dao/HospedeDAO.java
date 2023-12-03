package br.ufc.quixada.dao;

import br.ufc.quixada.entity.Hospede;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface HospedeDAO {
    public List<Hospede> findByNome(String nome);

    Optional<Hospede> findByCpf(String cpf);

    Hospede findFirstByCpf(String cpf);

    List<Hospede> findByNomeStartingWithIgnoreCase(String str);

    List<Hospede> buscaPorNomeContendoString(String nome);

    List<Hospede> findAllByOrderByNome();

    long count();

    void save(Hospede hospede);

    void deleteById(String string);

    @Transactional
    void deleteByCpf(String cpf);

    Optional<Hospede> findById(Integer id);

    List<Hospede> findAll();

    Hospede findHospedeWithReservasByCpf(String cpf);

    Hospede buscaPorCpfViaConsultaNomeada(String cpf);
}

