package br.ufc.quixada.dao.jpa;

import br.ufc.quixada.entity.Hospede;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospedeJPADAO extends JpaRepository<Hospede, Integer> {
  
  // Encontra o primeiro hóspede com o CPF especificado
  public Hospede findFirstByCpf(String cpf);

  // Consulta JPQL para encontrar o primeiro hóspede com o CPF especificado
  @Query("select h from Hospede h where h.cpf = :cpf")
  public Hospede buscaPrimeiroPorCpf(String cpf);

  // Consulta nativa para encontrar hóspedes com parte do CPF
  @Query(
    value = "SELECT * FROM hospedes WHERE cpf LIKE %:cpfPart%",
    nativeQuery = true
  )
  List<Hospede> findNativeByCpfPart(@Param("cpfPart") String cpfPart);

  // Consulta JPQL para encontrar um hóspede com reservas pelo CPF
  @Query(
    "SELECT h FROM Hospede h LEFT JOIN FETCH h.reservas WHERE h.cpf = :cpf"
  )
  Hospede findHospedeWithReservasByCpf(@Param("cpf") String cpf);

  // Consulta JPQL para encontrar todos os hóspedes ordenados pelo nome (ignorando maiúsculas/minúsculas)
  @Query("SELECT h FROM Hospede h ORDER BY LOWER(h.nome)")
  List<Hospede> findAllOrderedIgnoreCase();

  // Consulta nomeada para encontrar um hóspede pelo CPF
  @Query(name = "hospedePorCpf")
  public Hospede buscaPorCpfViaConsultaNomeada(String cpf);

  // Consulta baseada no nome do método para encontrar hóspedes com nomes que começam com a string fornecida (ignorando maiúsculas/minúsculas)
  public List<Hospede> findByNomeStartingWithIgnoreCase(String str);

  // Consulta JPQL para encontrar hóspedes cujos nomes contenham a string fornecida (ignorando maiúsculas/minúsculas)
  @Query(
    "select h from Hospede h where lower(h.nome) like lower(concat('%', :nome, '%'))"
  )
  public List<Hospede> buscaPorNomeContendoString(String nome);

  // Conta o número total de hóspedes
  public Long countBy();
}
