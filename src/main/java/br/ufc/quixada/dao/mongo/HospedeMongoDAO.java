package br.ufc.quixada.dao.mongo;

import br.ufc.quixada.dao.HospedeDAO;
import br.ufc.quixada.entity.Hospede;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospedeMongoDAO
  extends HospedeDAO, MongoRepository<Hospede, String> {
  public List<Hospede> findByNome(String nome);

  // Encontra o primeiro hóspede com o CPF especificado
  Hospede findFirstByCpf(String cpf);

  // Consulta nativa para encontrar hóspedes com parte do CPF
  @Query("{'cpf': {$regex : ?0, $options: 'i'}}")
  List<Hospede> findNativeByCpfPart(String cpfPart);

  // Consulta para encontrar um hóspede com reservas pelo CPF
  @Query("{'cpf': ?0}")
  Hospede findHospedeWithReservasByCpf(String cpf);

  // Consulta para encontrar todos os hóspedes ordenados pelo nome (ignorando maiúsculas/minúsculas)
  List<Hospede> findAllByOrderByNome();

  // Consulta para encontrar um hóspede pelo CPF
  @Query("{'cpf': ?0}")
  Hospede buscaPorCpfViaConsultaNomeada(String cpf);

  // Consulta baseada no nome do método para encontrar hóspedes com nomes que começam com a string fornecida (ignorando maiúsculas/minúsculas)
  List<Hospede> findByNomeStartingWithIgnoreCase(String str);

  // Consulta para encontrar hóspedes cujos nomes contenham a string fornecida (ignorando maiúsculas/minúsculas)
  @Query("{'nome': {$regex : ?0, $options: 'i'}}")
  List<Hospede> buscaPorNomeContendoString(String nome);

  void deleteByCpf(String cpf);

  // Conta o número total de hóspedes
  long count();
}
