package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Personagem;

public interface IPersonagemRepository extends CrudRepository<Personagem, Integer>
{
	@Query("SELECT P from Personagem P WHERE P.id = :param1")
	Optional<Personagem> findById(@Param("param1") int id);

	@Query("SELECT P.id, P.nome, P.idGenero, P.generoObrig, P.idEtnia, P.etniaObrig, P.idFaixaEtaria, P.faixaEtariaObrig, P.idFaixaPeso, P.faixaPesoObrig, P.idFaixaEstatura, P.faixaEstaturaObrig FROM Personagem P WHERE P.novela.id = :param1 ORDER BY P.nome")
	List<Personagem> findByPrioridadesPerfis(@Param("param1") int id);
}