package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Autor;
import br.com.apiremakenovelas.entities.VersaoNovela;

public interface IAutorRepository extends CrudRepository<Autor, Integer>
{
	@Query("SELECT A FROM Autor A ORDER BY A.nomeArtistico")
	List<Autor> findAll();

	@Query("SELECT (A.idGenero || ';' || COUNT(A.id)) FROM Autor A GROUP BY A.idGenero ORDER BY A.idGenero")
	List<String> countByGenero();

	@Query("SELECT CONCAT(N.id, '|', N.titulo, '|', replace(GROUP_CONCAT(Year(V.dataLancamento)), ',', ' / ')) FROM Novela N INNER JOIN VersaoNovela V ON V.versaoOriginal.id = N.id WHERE V.autor.id = :param1 GROUP BY N.id, N.titulo ORDER BY N.titulo")
	List<String> findNovelas(@Param("param1") int autorId);

	@Query("SELECT A FROM Autor A WHERE Upper(A.nomeArtistico) = Upper(:param1)")
	Optional<Autor> findByNomeArtistico(@Param("param1") String nomeArtistico);
}