package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Ator;
import br.com.apiremakenovelas.entities.ValorPorGrupo;

public interface IAtorRepository extends CrudRepository<Ator, Integer>
{
	@Query("SELECT A FROM Ator A ORDER BY A.nomeArtistico")
	List<Ator> findAll();

	@Query("SELECT A FROM Ator A WHERE A.id = :param1")
	Optional<Ator> findById(@Param("param1") int id);

	@Query("SELECT A FROM Ator A WHERE Upper(A.nomeArtistico) = Upper(:param1)")
	Optional<Ator> findByNomeArtistico(@Param("param1") String nomeArtistico);

	@Query("SELECT (A.idGenero || ';' || COUNT(A.id)) FROM Ator A GROUP BY A.idGenero ORDER BY A.idGenero")
	List<String> countByGenero();

	@Query("SELECT (A.idEtnia || ';' || COUNT(A.id)) FROM Ator A GROUP BY A.idEtnia ORDER BY A.idEtnia")
	List<String> countByEtnia();

	@Query("SELECT (A.idFaixaEtaria || ';' || COUNT(A.id)) as quantidade FROM Ator A GROUP BY A.idFaixaEtaria ORDER BY A.idFaixaEtaria")
	List<String> countByFaixaEtaria();

	@Query("SELECT (A.idFaixaPeso || ';' || COUNT(A.id)) FROM Ator A GROUP BY A.idFaixaPeso ORDER BY A.idFaixaPeso")
	List<String> countByFaixaPeso();

	@Query("SELECT (A.idFaixaEstatura || ';' || COUNT(A.id)) FROM Ator A GROUP BY A.idFaixaEstatura ORDER BY A.idFaixaEstatura")
	List<String> countByFaixaEstatura();
}