package br.com.apiremakenovelas.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import br.com.apiremakenovelas.entities.Autor;

public interface IAutorRepository extends CrudRepository<Autor, Integer>
{
	@Query("SELECT A FROM Autor A ORDER BY A.nomeArtistico")
	List<Autor> findAll();
}