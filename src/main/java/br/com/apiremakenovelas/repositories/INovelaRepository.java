package br.com.apiremakenovelas.repositories;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Novela;
import br.com.apiremakenovelas.entities.Personagem;
import br.com.apiremakenovelas.entities.VersaoNovela;

public interface INovelaRepository extends CrudRepository<Novela, Integer>
{
    @Query("SELECT N FROM Novela N")
	List<Novela> findAll();

	@Query("SELECT V FROM VersaoNovela V WHERE V.versaoOriginal.id = :param1 ORDER BY V.dataLancamento")
	List<VersaoNovela> getVersoes(@Param("param1") int id);

	@Query("SELECT P FROM Personagem P WHERE P.novela.id = :param1")
	List<Personagem> getPersonagens(@Param("param1") int id);

	@Query("SELECT N FROM Novela N INNER JOIN VersaoNovela V ON V.versaoOriginal.id = N.id INNER JOIN Autor A ON V.autor.id = A.id ORDER BY N.titulo")
	List<String> findRemakes();

    @Query("SELECT MAX(N.id) FROM Novela N")
	Integer getMaxId();
    
    @Modifying
    @Query(value = "INSERT INTO VersaoNovela (id_versao_original, datalancamento, qtdcapitulos, imagemlogotipo, elencocompleto, id_autor) VALUES (:param1, :param2, :param3, :param4, :param5, :param6)", nativeQuery = true)
    @Transactional
    void insertVersoes(@Param("param1") int versaoOriginalId, @Param("param2") Date dataLancamento, @Param("param3") int qtdCapitulos, @Param("param4") String imagemLogotipo, @Param("param5") boolean elencoCompleto, @Param("param6") int autorId);

    @Modifying
    @Query(value = "INSERT INTO Personagem (id_novela, nome, id_genero, id_etnia, id_faixa_etaria, id_faixa_peso, id_faixa_estatura, generoobrig, etniaobrig, faixaetariaobrig, faixapesoobrig, faixaestaturaobrig) VALUES (:param1, :param2, :param3, :param4, :param5, :param6, :param7, 0, 0, 0, 0, 0)", nativeQuery = true)
    @Transactional
    void insertPersonagens(@Param("param1") int novelaId, @Param("param2") String nome, @Param("param3") int idGenero, @Param("param4") int idEtnia, @Param("param5") int idFaixaEtaria, @Param("param6") int idFaixaPeso, @Param("param7") int idFaixaEstatura);
    
    @Modifying
    @Query(value = "UPDATE VersaoNovela SET id_versao_original = :param1, datalancamento = :param2, qtdcapitulos = :param3, imagemlogotipo = :param4, elencocompleto = :param5, id_autor = :param6 WHERE id = :param7", nativeQuery = true)
    @Transactional
    void updateVersoes(@Param("param1") int versaoOriginalId, @Param("param2") Date dataLancamento, @Param("param3") int qtdCapitulos, @Param("param4") String imagemLogotipo, @Param("param5") boolean elencoCompleto, @Param("param6") int autorId, @Param("param7") int id);

    @Modifying
    @Query(value = "UPDATE Personagem SET id_novela = :param1, nome = :param2, id_genero = :param3, id_etnia = :param4, id_faixa_etaria = :param5, id_faixa_peso = :param6, id_faixa_estatura = :param7 WHERE id = :param8", nativeQuery = true)
    @Transactional
    void updatePersonagens(@Param("param1") int novelaId, @Param("param2") String nome, @Param("param3") int idGenero, @Param("param4") int idEtnia, @Param("param5") int idFaixaEtaria, @Param("param6") int idFaixaPeso, @Param("param7") int idFaixaEstatura, @Param("param8") int id);

	@Query("DELETE FROM VersaoNovela WHERE id_versao_original = :param1")
	void deleteVersoes(@Param("param1") int id);

	@Query("DELETE FROM Personagem WHERE id_novela = :param1")
	void deletePersonagens(@Param("param1") int id);
}

/*
// get_personagens/{novelaId:int}
resultado.add(dadosPersonagensComInterpretes);
resultado.add(dadosVersoesNovelas.Aggregate(",", (current, next) => current + "," + next).Replace(",,", ""));
resultado.add(SELECT A.id + ";" + A.NomeArtistico + ";" + "SEM_FOTO" FROM Ator A ORDER BY A.NomeArtistico);
resultado.add(SELECT V.id FROM VersaoNovela V WHERE V.ElencoCompleto = false AND V.versaoOriginal.id = @novelaId);        
resultado.add(SELECT N.Titulo FROM Novela N WHERE N.id = @novelaId);

// get_itens_dashboard_profissionais
            var qtdGeneros = (await Db.Generos.ToListAsync()).Count().ToString();

    var porGenero = await (from ator in Db.Atores
                           join genero in Db.Generos on ator.GeneroId equals genero.Id
                           group ator by new { ator.Genero.Descricao } into grupo
                           select new { DescricaoQuantidade = grupo.Key.Descricao + ";" + grupo.Count().ToString() }).ToListAsync();

    dados = "|";
    foreach (var item in porGenero)
        dados += "|" + item.DescricaoQuantidade;
    resultado.Add(dados.Replace("||", ""));

    var porEtnia = await (from ator in Db.Atores
                          join etnia in Db.Etnias on ator.EtniaId equals etnia.Id
                          group ator by new { ator.Etnia.Descricao } into grupo
                          select new { DescricaoQuantidade = grupo.Key.Descricao + ";" + grupo.Count().ToString() }).ToListAsync();

    dados = "|";
    foreach (var item in porEtnia)
        dados += "|" + item.DescricaoQuantidade;
    resultado.Add(dados.Replace("||", ""));

    var porFaixaEtaria = await (from ator in Db.Atores
                                join faixaEtaria in Db.FaixasEtarias on ator.FaixaEtariaId equals faixaEtaria.Id
                                group ator by new { ator.FaixaEtaria.IdadeMinima, ator.FaixaEtaria.IdadeMaxima } into grupo
                                select new
                                {
                                    DescricaoQuantidade = (grupo.Key.IdadeMinima == 0 ? "Até ": grupo.Key.IdadeMinima.ToString() + " a ") + grupo.Key.IdadeMaxima.ToString() + " anos;" + grupo.Count().ToString()
                                }).ToListAsync();

    dados = "|";
    foreach (var item in porFaixaEtaria)
        dados += "|" + item.DescricaoQuantidade;
    resultado.Add(dados.Replace("||", ""));

    var porFaixaPeso = await (from ator in Db.Atores
                              join faixaPeso in Db.FaixasPeso on ator.FaixaPesoId equals faixaPeso.Id
                              group ator by new { ator.FaixaPeso.PesoMinimo, ator.FaixaPeso.PesoMaximo } into grupo
                              select new
                              {
                                  DescricaoQuantidade = (grupo.Key.PesoMinimo == 0 ? "Até " : grupo.Key.PesoMinimo.ToString().Replace(".", ",") + " kg a ") + grupo.Key.PesoMaximo.ToString().Replace(".", ",") + " kg;" + grupo.Count().ToString()
                              }).ToListAsync();

    dados = "|";
    foreach (var item in porFaixaPeso)
        dados += "|" + item.DescricaoQuantidade;
    resultado.Add(dados.Replace("||", ""));

    var porFaixaEstatura = await (from ator in Db.Atores
                                  join faixaEstatura in Db.FaixasEstaturas on ator.FaixaEstaturaId equals faixaEstatura.Id
                                  group ator by new { ator.FaixaEstatura.EstaturaMinima, ator.FaixaEstatura.EstaturaMaxima } into grupo
                                  select new
                                  {
                                      DescricaoQuantidade = (grupo.Key.EstaturaMinima == 0 ? "Até " : grupo.Key.EstaturaMinima.ToString().Replace(".", ",") + " m a ") + grupo.Key.EstaturaMaxima.ToString().Replace(".", ",") + " m;" + grupo.Count().ToString()
                                  }).ToListAsync();

    dados = "|";
    foreach (var item in porFaixaEstatura)
        dados += "|" + item.DescricaoQuantidade;
    resultado.Add(dados.Replace("||", ""));

    resultado.Add(qtdGeneros);
    return resultado;

 */