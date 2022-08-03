package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Autor;

public interface IAutorRepository extends CrudRepository<Autor, Integer>
{
	@Query("SELECT A FROM Autor A ORDER BY A.nomeArtistico")
	List<Autor> findAll();

	@Query("SELECT A FROM Autor A WHERE A.id = :param1")
	Optional<Autor> findById(@Param("param1") int id);
}