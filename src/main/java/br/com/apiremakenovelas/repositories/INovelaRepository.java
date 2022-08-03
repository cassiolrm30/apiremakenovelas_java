package br.com.apiremakenovelas.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import br.com.apiremakenovelas.entities.Novela;

public interface INovelaRepository extends CrudRepository<Novela, Integer>
{
	@Query("SELECT N FROM Novela N INNER JOIN VersaoNovela V ON V.versaoOriginal.id = N.id INNER JOIN Autor A ON V.autor.id = A.id ORDER BY N.titulo")
	List<Novela> findAll();

	@Query("SELECT N FROM Novela N INNER JOIN VersaoNovela V ON V.versaoOriginal.id = N.id INNER JOIN Autor A ON V.autor.id = A.id WHERE N.id = :param1")
	Optional<Novela> findById(@Param("param1") int id);

	@Query("SELECT N.id, N.titulo FROM Novela N INNER JOIN Personagem P ON P.novela.id = N.id ORDER BY N.titulo")
	Optional<Novela> findWithPersonagens();

	@Query("SELECT N FROM Novela N INNER JOIN VersaoNovela V ON V.versaoOriginal.id = N.id INNER JOIN Autor A ON V.autor.id = A.id ORDER BY N.titulo")
	List<String> findRemakes();

	@Query("DELETE FROM VersaoNovela WHERE novelaId = :param1")
	void deleteVersoes(@Param("param1") int id);

	@Query("DELETE FROM Personagem WHERE novelaId = :param1")
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
