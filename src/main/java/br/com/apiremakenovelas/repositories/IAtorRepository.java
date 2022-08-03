package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Ator;

public interface IAtorRepository extends CrudRepository<Ator, Integer>
{
	@Query("SELECT A FROM Ator A ORDER BY A.nomeArtistico")
	List<Ator> findAll();

	@Query("SELECT A FROM Ator A WHERE A.id = :param1")
	Optional<Ator> findById(@Param("param1") int id);
}