package br.com.apiremakenovelas.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import br.com.apiremakenovelas.entities.Autor;
import br.com.apiremakenovelas.entities.Novela;
import br.com.apiremakenovelas.entities.Personagem;
import br.com.apiremakenovelas.entities.VersaoNovela;
import br.com.apiremakenovelas.enums.Etnia;
import br.com.apiremakenovelas.enums.FaixaEstatura;
import br.com.apiremakenovelas.enums.FaixaEtaria;
import br.com.apiremakenovelas.enums.FaixaPeso;
import br.com.apiremakenovelas.enums.Genero;
import br.com.apiremakenovelas.requests.NovelaRequest;
import br.com.apiremakenovelas.responses.NovelaGetResponse;
import br.com.apiremakenovelas.responses.RemakeGetResponse;
import br.com.apiremakenovelas.repositories.IAtorRepository;
import br.com.apiremakenovelas.repositories.IAutorRepository;
import br.com.apiremakenovelas.repositories.INovelaRepository;
import br.com.apiremakenovelas.repositories.IPersonagemRepository;
import br.com.apiremakenovelas.repositories.IVersaoNovelaRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class NovelaController
{
	@Autowired
	private INovelaRepository novelaRepository;
	private IAtorRepository atorRepository;
	private IVersaoNovelaRepository versaoNovelaRepository;
	private IPersonagemRepository personagemRepository;
	private IAutorRepository autorRepository;
	private final SimpleDateFormat simpleDateFormatMySQL = new SimpleDateFormat("yyyy-MM-dd");
    private final String separador = "|";

	@CrossOrigin
	@ApiOperation("Endpoint para consulta de novelas.")
	@RequestMapping(value = "/api/novela", method = RequestMethod.GET)
	public ResponseEntity<List<NovelaGetResponse>> get()
	{	
		try
		{	
			List<NovelaGetResponse> lista = new ArrayList<NovelaGetResponse>();

			// consultando os registros existentes no banco de dados
			for (Novela registro : novelaRepository.findAll())
			{
				NovelaGetResponse response = new NovelaGetResponse();
				response.setId(registro.getId());
				response.setTitulo(registro.getTitulo().toUpperCase());
				response.setAutorOriginal("");
				response.setAutorAtual("");
				lista.add(response);
			}

			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body(lista);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@CrossOrigin
	@ApiOperation("Endpoint para consulta de 1 registro.")
	@RequestMapping(value = "/api/novela/{id}", method = RequestMethod.GET)
	public ResponseEntity<NovelaGetResponse> get(@PathVariable int id)
	{
		try
		{
			NovelaGetResponse resultado = new NovelaGetResponse();
			Optional<Novela> novelaRepositoryAux = novelaRepository.findById(id);
			if (novelaRepositoryAux != null)
			{
				Novela registro = novelaRepositoryAux.get();
				NovelaGetResponse response = new NovelaGetResponse();
	            String dadosVersoes = "", dadosPersonagens = "";
				response.setId(id);
				response.setTitulo(registro.getTitulo().toUpperCase());
				response.setSinopse(registro.getSinopse());

				List<VersaoNovela> versoes = registro.getVersoes();
				int indice = 1;
				for (VersaoNovela item : versoes)
				{
	            	dadosVersoes += indice + "ª" + separador;
	            	dadosVersoes += item.getAutor().getNomeArtistico().toUpperCase() + separador;
	            	dadosVersoes += simpleDateFormatMySQL.format(item.getDataLancamento()) + separador;
	            	dadosVersoes += item.getQtdCapitulos().toString() + separador;
	            	dadosVersoes += item.getId().toString();
	                if (indice < versoes.size())
	                    dadosVersoes += separador + separador;
	                indice++;
	            }
				response.setDadosVersoes(dadosVersoes);

				List<Personagem> personagens = registro.getPersonagens();
				for (Personagem item : personagens)
	            {
	                dadosPersonagens += item.getNome() + separador;
	                for (Genero subitem : Genero.values())
	                {
	                	if (subitem.getId() == item.getIdGenero())
	                	{
	    	                dadosPersonagens += subitem.getDescricao() + separador;
	                		break;
	                	}
	                }
	                for (Etnia subitem : Etnia.values())
	                {
	                	if (subitem.getId() == item.getIdEtnia())
	                	{
	    	                dadosPersonagens += subitem.getDescricao() + separador;
	                		break;
	                	}
	                }
	                for (FaixaEtaria subitem : FaixaEtaria.values())
	                {
	                	if (subitem.getId() == item.getIdFaixaEtaria())
	                	{
	    	                dadosPersonagens += subitem.getDescricao() + separador;
	                		break;
	                	}
	                }
	                for (FaixaPeso subitem : FaixaPeso.values())
	                {
	                	if (subitem.getId() == item.getIdFaixaPeso())
	                	{
	    	                dadosPersonagens += subitem.getDescricao() + separador;
	                		break;
	                	}
	                }
	                for (FaixaEstatura subitem : FaixaEstatura.values())
	                {
	                	if (subitem.getId() == item.getIdFaixaEstatura())
	                	{
	    	                dadosPersonagens += subitem.getDescricao() + separador;
	                		break;
	                	}
	                }
	                dadosPersonagens += item.getId().toString();
	                if (indice < personagens.size())
	                    dadosVersoes += separador + separador;
	                indice++;
	            }
				response.setDadosPersonagens(dadosPersonagens);
			}

			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body(resultado);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	@CrossOrigin
	@ApiOperation("Endpoint para consulta de novelas com personagens.")
	@RequestMapping(value = "/api/novela/get_novelas_com_personagens", method = RequestMethod.GET)
	public ResponseEntity<String[]> getNovelasComPersonagens()
	{
		try
		{
			String[] itens;
			String conteudo = "";
			List<Novela> novelas = novelaRepository.findAll();
            for (var registro : novelas)
            {
            	if (registro.getPersonagens().size() > 0)
            		conteudo += registro.getId() + "|" + registro.getTitulo() + ";";
            }
            conteudo += ";";
            conteudo = conteudo.replace(";;", "");
            itens = conteudo.split(";");

			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body(itens);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}		
	}
	
	@CrossOrigin
	@ApiOperation("Endpoint para consulta de personagens de uma novela com intérpretes anteriores e atuais.")
	@RequestMapping(value = "/api/novela/get_personagens/{id}", method = RequestMethod.GET)
	public ResponseEntity<RemakeGetResponse> getRemakes(@PathVariable int id)
	{
		try
		{
			RemakeGetResponse resultado = new RemakeGetResponse();
			Optional<Novela> dadosNovela = novelaRepository.findById(id);
        	//String item = "";
			if (dadosNovela != null)
			{
				//var dadosVersoes = dadosNovela.get().getVersoes();
	            resultado.setTituloNovela(dadosNovela.get().getTitulo());
				/*
	            item = "";
	        	int idVersaoAtual = 0;
	            for (var registro : dadosVersoes)
	            {
		        	int idVersao = registro.getId();
	            	if (idVersao > idVersaoAtual)
	            		idVersaoAtual = idVersao;
	            	item = idVersao + "|" + (registro.isElencoCompleto() ? "1" : "0") + "|" + registro.getDataLancamento().getYear() + "|";
	            }
	        	item = item.replace("||", "");
	            resultado.setDadosVersoes(item.split("|"));
	            resultado.setIdVersaoAtual(idVersaoAtual);
				
				var dadosPersonagens = dadosNovela.get().getPersonagens();
	        	item = "";
	            for (var registro : dadosPersonagens)
	            {
	            	item = registro.getId() + "|";
	            }
	        	item = item.replace("||", "");
	            resultado.setDadosPersonagens(item.split("|"));

	            var dadosAtores = atorRepository.findAll();
	            for (var registro : dadosAtores)
	            {
	            	item = registro.getId() + ";" + registro.getNomeArtistico() + ";";
	            	item += (registro.getImagemfoto() == null ? "SEM_FOTO" : "atores/" + registro.getNomeArtistico().replace(" ", "_").toUpperCase());
	            	item += "|";
	            }
	        	item = item.replace("||", "");
	            resultado.setDadosAtores(item.split("|"));
	            */
			}

			var x = new String[2]; x[0] = "11|1|1976"; x[1] = "12|0|1997";
			var y = new String[2]; y[0] = "EUNICE MACHADO (NICE)||1|2||8|85|80|67|78|16|15|63|38|21|2|17|6|29|82|26|20|72|74|76|24|81|12|77|37|9|31|79|19|84|1|35|66|13|11||2"; y[1] = "RODRIGO MEDEIROS||3|4||30|64|70|18|33|22|39|34|3|4|68|69|71|10|73|25|75|14|27|83|28|7|23||3";
			var z = new String[5]; z[0] = "8;Alessandra Negrini;atores/ALESSANDRA_NEGRINI"; z[1] = "30;Átila Iório;SEM_FOTO"; z[2] = "85;Beth Goulart;atores/BETH_GOULART"; z[3] = "80;Bruna Linzmeyer;atores/BRUNA_LINZMEYER"; z[4] = "67;Camila Pitanga;atores/CAMILA_PITANGA";
			resultado.setDadosPersonagens(y);
			resultado.setDadosVersoes(x);
			resultado.setDadosAtores(z);
			resultado.setIdVersaoAtual(12);
			resultado.setTituloNovela("");
			
			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body(resultado);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@CrossOrigin
	@ApiOperation("Endpoint para cadastro de novelas.")
	@RequestMapping(value = "/api/novela", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody NovelaRequest request)
	{
		try
		{
			// capturando e salvando os dados
			Novela newRegistro = new Novela();
			newRegistro.setTitulo(request.getTitulo());
			newRegistro.setSinopse(request.getSinopse());
			novelaRepository.save(newRegistro);

			int novelaId = novelaRepository.getMaxId();

			newRegistro.setId(novelaId);
            var dadosVersoes = request.getDadosVersoes();
            for (String[] item : dadosVersoes)
            {
                var versao = new VersaoNovela();
                versao.setDataLancamento(simpleDateFormatMySQL.parse(item[2]));
                versao.setQtdCapitulos(Integer.parseInt(item[3]));
                versao.setImagemLogotipo("");
                versao.setElencoCompleto(false);
                versao.getAutor().setId(Integer.parseInt(item[1]));
                versao.setVersaoOriginal(newRegistro);
                novelaRepository.insertVersoes(versao.getVersaoOriginal().getId(), versao.getDataLancamento(), versao.getQtdCapitulos(), versao.getImagemLogotipo(), false, versao.getAutor().getId());
            }

            var dadosPersonagens = request.getDadosPersonagens();
            for (String[] item : dadosPersonagens)
            {
                var personagem = new Personagem();
                personagem.setNome(item[0]);
                personagem.setNovela(newRegistro);
                personagem.setIdGenero(Integer.parseInt(item[1]));
                personagem.setIdEtnia(Integer.parseInt(item[2]));
                personagem.setIdFaixaEtaria(Integer.parseInt(item[3]));
                personagem.setIdFaixaPeso(Integer.parseInt(item[4]));
                personagem.setIdFaixaEstatura(Integer.parseInt(item[5]));
                novelaRepository.insertPersonagens(personagem.getNovela().getId(), personagem.getNome(), personagem.getIdGenero(), personagem.getIdEtnia(), personagem.getIdFaixaEtaria(), personagem.getIdFaixaPeso(), personagem.getIdFaixaEstatura());
            }
			
			// HTTP 201 (CREATED)
			return ResponseEntity.status(HttpStatus.CREATED).body("Dados salvos com sucesso.");
		}
		catch(IllegalArgumentException e)
		{
			// HTTP 400 (CLIENT ERROR) -> BAD REQUEST
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(Exception e)
		{
			// HTTP 500 (SERVER ERROR) -> INTERNAL SERVER ERROR
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@CrossOrigin
	@ApiOperation("Endpoint para atualização de novelas.")
	@RequestMapping(value = "/api/novela", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody NovelaRequest request)
	{
		try
		{
			Optional<Novela> oldRegistro = novelaRepository.findById(request.getId());
	        if (!oldRegistro.isPresent())
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");

	        // capturando e salvando os dados
			Novela newRegistro = oldRegistro.get();
			newRegistro.setTitulo(request.getTitulo());
			newRegistro.setSinopse(request.getSinopse());
			newRegistro.setId(request.getId());

            var dadosVersoes = request.getDadosVersoes();
            for (String[] item : dadosVersoes)
            {
                //String[] dadosItem = item.split("|");
    			Optional<Autor> autor = autorRepository.findById(Integer.parseInt(item[1]));
                if (autor != null)
                {
                    var versao = new VersaoNovela();
                    versao.setAutor(autor.get());
                    versao.setId(Integer.parseInt(item[4]));
                    //versao.setDataLancamento(Date.parse(dadosItem[2]));
                    versao.setQtdCapitulos(Integer.parseInt(item[3]));
                    versao.setVersaoOriginal(newRegistro);
                    newRegistro.getVersoes().add(versao);
                }
            }

            var dadosPersonagens = request.getDadosPersonagens();
            for (String[] item : dadosPersonagens)
            {
                //String[] dadosItem = item.split("|");
                /*int[] idsPerfis = await _repositorioPerfil.GetPerfisByDescription(dadosItem[1], dadosItem[2], dadosItem[3], dadosItem[4], dadosItem[5]);
                var personagem = new Personagem()
                {
                    Id = int.Parse(dadosItem[6]),  Nome = dadosItem[0],
                    GeneroId = idsPerfis[0], EtniaId = idsPerfis[1],
                    FaixaEtariaId = idsPerfis[2], FaixaPesoId = idsPerfis[3],
                    FaixaEstaturaId = idsPerfis[4], NovelaId = model.Id
                };*/
                var personagem = new Personagem();
                personagem.setNome(item[0]);
                personagem.setNovela(newRegistro);
                personagem.setIdGenero(Integer.parseInt(item[0]));
                personagem.setIdEtnia(Integer.parseInt(item[1]));
                personagem.setIdFaixaEtaria(Integer.parseInt(item[2]));
                personagem.setIdFaixaPeso(Integer.parseInt(item[3]));
                personagem.setIdFaixaEstatura(Integer.parseInt(item[4]));
                newRegistro.getPersonagens().add(personagem);
            }
            novelaRepository.save(newRegistro);

        	// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body("Dados salvos com sucesso.");
		}
		catch (IllegalArgumentException e)
		{
			// HTTP 400 (CLIENT ERROR) -> BAD REQUEST
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(Exception e)
		{
			// HTTP 500 (SERVER ERROR) -> INTERNAL SERVER ERROR
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@CrossOrigin
	@ApiOperation("Endpoint para exclusão de novelas e suas associações.")
	@RequestMapping(value = "/api/novela/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id)
	{
		try
		{
			Optional<Novela> registro = novelaRepository.findById(id);
			if (!registro.isPresent())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
			novelaRepository.deletePersonagens(id);
			novelaRepository.deleteVersoes(id);			
			novelaRepository.delete(registro.get());

			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body("Dados excluídos com sucesso.");	
		}
		catch (IllegalArgumentException e)
		{
			// HTTP 400 (CLIENT ERROR) -> BAD REQUEST
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e)
		{
			// HTTP 500 (SERVER ERROR) -> INTERNAL SERVER ERROR
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}