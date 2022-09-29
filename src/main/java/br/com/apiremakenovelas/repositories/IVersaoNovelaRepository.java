package br.com.apiremakenovelas.repositories;
import org.springframework.data.repository.CrudRepository;
import br.com.apiremakenovelas.entities.VersaoNovela;

public interface IVersaoNovelaRepository extends CrudRepository<VersaoNovela, Integer>
{
	void deleteById(int id);	
}