package br.com.apiremakenovelas.controllers;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import br.com.apiremakenovelas.entities.Ator;
import br.com.apiremakenovelas.entities.Autor;
import br.com.apiremakenovelas.entities.Interpretacao;
import br.com.apiremakenovelas.entities.Novela;
import br.com.apiremakenovelas.entities.Personagem;
import br.com.apiremakenovelas.entities.ValorPorGrupo;
import br.com.apiremakenovelas.entities.VersaoNovela;
import br.com.apiremakenovelas.enums.Etnia;
import br.com.apiremakenovelas.enums.FaixaEstatura;
import br.com.apiremakenovelas.enums.FaixaEtaria;
import br.com.apiremakenovelas.enums.FaixaPeso;
import br.com.apiremakenovelas.enums.Genero;
import br.com.apiremakenovelas.requests.NovelaRequest;
import br.com.apiremakenovelas.requests.PersonagemRequest;
import br.com.apiremakenovelas.responses.NovelaGetResponse;
import br.com.apiremakenovelas.responses.PersonagemGetResponse;
import br.com.apiremakenovelas.responses.RemakeGetResponse;
import br.com.apiremakenovelas.repositories.IAtorRepository;
import br.com.apiremakenovelas.repositories.IAutorRepository;
import br.com.apiremakenovelas.repositories.IInterpretacaoRepository;
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
	private IVersaoNovelaRepository versaoNovelaRepository;
	private IPersonagemRepository personagemRepository;
	private IInterpretacaoRepository interpretacaoRepository;
	@Autowired
	private IAtorRepository atorRepository;	
	@Autowired
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
				var versoes = registro.getVersoes().toArray();
				NovelaGetResponse response = new NovelaGetResponse();
				response.setId(registro.getId());
				response.setTitulo(registro.getTitulo().toUpperCase());
				if (versoes.length > 0)
				{
					Autor autorOriginal = ((VersaoNovela)versoes[0]).getAutor();
					response.setAutorOriginal(autorOriginal == null ? "-" : autorOriginal.getNomeArtistico().toUpperCase());
					if (versoes.length > 1)
					{
						Autor autorAtual = ((VersaoNovela)versoes[versoes.length-1]).getAutor();
						response.setAutorAtual(autorAtual == null ? "-" : autorAtual.getNomeArtistico().toUpperCase());
					}
				}
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
	@ApiOperation("Endpoint para consulta de perfis de personagens.")
	@RequestMapping(value = "/api/novela/get_prioridades_perfis", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getPerfisPrioridades(int novelaId)
	{	
		try
		{	
			List<String> resultado = new ArrayList<String>();
			Optional<Novela> novelaRepositoryAux = novelaRepository.findById(novelaId);
			if (!novelaRepositoryAux.isPresent())
			{
				// HTTP 404 (Not Found)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);				
			}
			List<Personagem> personagens = novelaRepositoryAux.get().getPersonagens();
			for (Personagem registro : personagens)
			{
				String item = registro.getId().toString() + separador + registro.getNome();
				item += separador + registro.getIdGenero().toString() 		 + separador + (registro.isGeneroObrig() ? "1" : "0");
				item += separador + registro.getIdEtnia().toString() 		 + separador + (registro.isEtniaObrig() ? "1" : "0");
				item += separador + registro.getIdFaixaEtaria().toString() 	 + separador + (registro.isFaixaEtariaObrig() ? "1" : "0");
				item += separador + registro.getIdFaixaPeso().toString() 	 + separador + (registro.isFaixaPesoObrig() ? "1" : "0");
				item += separador + registro.getIdFaixaEstatura().toString() + separador + (registro.isFaixaEstaturaObrig() ? "1" : "0");
				resultado.add(item);
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
	@ApiOperation("Endpoint para consulta de 1 registro.")
	@RequestMapping(value = "/api/novela/{id}", method = RequestMethod.GET)
	public ResponseEntity<NovelaGetResponse> get(@PathVariable int id)
	{
		try
		{
			NovelaGetResponse resultado = new NovelaGetResponse();
			Optional<Novela> novelaRepositoryAux = novelaRepository.findById(id);
			if (novelaRepositoryAux.isPresent())
			{
				Novela registro = novelaRepositoryAux.get();
	            String dadosVersoes = "", dadosPersonagens = "";
	            resultado.setId(id);
	            resultado.setTitulo(registro.getTitulo().toUpperCase());
				resultado.setSinopse(registro.getSinopse());

				List<VersaoNovela> versoes = novelaRepository.getVersoes(id);
				int indice = 1;
				for (VersaoNovela item : versoes)
				{
	            	dadosVersoes += indice + "ª" + separador;
	            	dadosVersoes += item.getAutor().getNomeArtistico().toUpperCase() + separador;
	            	dadosVersoes += simpleDateFormatMySQL.format(item.getDataLancamento()) + separador;
	            	dadosVersoes += item.getQtdCapitulos().toString() + separador;
	            	dadosVersoes += item.getId().toString() + separador + separador;
	                if (indice == 1)
	            		resultado.setAutorOriginal(item.getAutor().getNomeArtistico().toUpperCase());
	                else
	                	resultado.setAutorAtual(item.getAutor().getNomeArtistico().toUpperCase());	                	
	                indice++;
	            }
				if (!dadosVersoes.equals(""))
					dadosVersoes = dadosVersoes.substring(0, dadosVersoes.length()-2);
				resultado.setDadosVersoes(dadosVersoes);

				List<Personagem> personagens = novelaRepository.getPersonagens(id);
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
	                dadosPersonagens += item.getId().toString() + separador + separador;
	                indice++;
	            }
				if (!dadosPersonagens.equals(""))
					dadosPersonagens = dadosPersonagens.substring(0, dadosPersonagens.length()-2);
				resultado.setDadosPersonagens(dadosPersonagens);
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
	public ResponseEntity<String[]> getComPersonagens()
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
		RemakeGetResponse resultado = new RemakeGetResponse();
		try
		{
			Optional<Novela> dadosNovela = novelaRepository.findById(id);
			if (dadosNovela != null)
			{
				var listaDadosVersoes = new ArrayList<String>();
				
				// Parte do mês com valores de 0 a 11 para tratamento em JavaScript
				Date dataMaisAtual = new Date();
				dataMaisAtual.setYear(1); dataMaisAtual.setMonth(0); dataMaisAtual.setDate(1);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

	            for (VersaoNovela versao : dadosNovela.get().getVersoes())
	            {
					if (versao.getDataLancamento().compareTo(dataMaisAtual) > 0)
					{
						dataMaisAtual = versao.getDataLancamento();
						resultado.setIdVersaoAtual(versao.getId());
					}
	            	String item = versao.getId().toString();
	            	item += separador + (versao.isElencoCompleto() ? "1" : "0");
	            	item += separador + simpleDateFormat.format(versao.getDataLancamento());
					listaDadosVersoes.add(item);
	            }
				var dadosVersoes = new String[listaDadosVersoes.size()];
	            for (int i = 0; i < dadosVersoes.length; i++)
	            	dadosVersoes[i] = listaDadosVersoes.get(i);

				List<String> listaDadosPersonagens = new ArrayList<String>();
				String dadosAtoresSucessores = "";

				// Dados de personagens
	            for (Personagem personagem : dadosNovela.get().getPersonagens())
	            {
					// Dados de interpretações
	            	int interpretacaoId1 = 0;
	            	int interpretacaoId2 = 0;
		            //for (Interpretacao interpretacao : interpretacaoRepository.findAll())
		            //{
		            //	interpretacaoId1 = 0;
		            //	interpretacaoId2 = 0;
		            //}

					// Dados de atores
	            	dadosAtoresSucessores = separador;
		            for (Ator ator : atorRepository.findAll())
		            {
		            	int qtRequisitos = 0;
		            	//if (!personagem.isGeneroObrig() || (personagem.isGeneroObrig() && personagem.getIdGenero() == ator.getIdGenero()))
		            	//	qtRequisitos++;
		            	if (!personagem.isEtniaObrig() || (personagem.isEtniaObrig() && personagem.getIdEtnia() == ator.getIdEtnia()))
		            		qtRequisitos++;
		            	if (!personagem.isFaixaEtariaObrig() || (personagem.isFaixaEtariaObrig() && personagem.getIdFaixaEtaria() == ator.getIdFaixaEtaria()))
		            		qtRequisitos++;
		            	if (!personagem.isFaixaPesoObrig() || (personagem.isFaixaPesoObrig() && personagem.getIdFaixaPeso() == ator.getIdFaixaPeso()))
		            		qtRequisitos++;
		            	if (!personagem.isFaixaEstaturaObrig() || (personagem.isFaixaEstaturaObrig() && personagem.getIdFaixaEstatura() == ator.getIdFaixaEstatura()))
		            		qtRequisitos++;
		            	
		            	if (personagem.getIdGenero() == ator.getIdGenero() && qtRequisitos > 0)		            	
		            		dadosAtoresSucessores += separador + ator.getId();
		            }
	            	String item = personagem.getNome().toUpperCase();
	            	item += (separador + separador) + interpretacaoId1 + separador + interpretacaoId2 + dadosAtoresSucessores;
	            	item += (separador + separador) + personagem.getId();
    				listaDadosPersonagens.add(item);
	            }
				
				var dadosPersonagens = new String[listaDadosPersonagens.size()];
	            for (int i = 0; i < dadosPersonagens.length; i++)
	            	dadosPersonagens[i] = listaDadosPersonagens.get(i);

				// Dados de atores
				List<String> listaDadosAtores = new ArrayList<String>();
	            for (Ator ator : atorRepository.findAll())
	            {
	            	String item = ator.getId() + ";" + ator.getNomeArtistico() + ";";
	            	if (ator.getImagemFoto() == null || ator.getImagemFoto().equals(""))
		            	item += "SEM_FOTO";
	            	else
		            	item += "atores/" + ator.getNomeArtistico().toUpperCase().replace(" ", "_");
	            	listaDadosAtores.add(item);
	            }
				var dadosAtores = new String[listaDadosAtores.size()];
	            for (int i = 0; i < dadosAtores.length; i++)
	            	dadosAtores[i] = listaDadosAtores.get(i);

	            resultado.setTituloNovela(dadosNovela.get().getTitulo());
				resultado.setDadosVersoes(dadosVersoes);
				resultado.setDadosPersonagens(dadosPersonagens);
				resultado.setDadosAtores(dadosAtores);
			}
			// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body(resultado);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			resultado.setTituloNovela(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
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
                Optional<Autor> autor = autorRepository.findByNomeArtistico(item[1]);
                if (autor.isPresent())
                {
                    versao.setAutor(autor.get());
                    versao.setDataLancamento(simpleDateFormatMySQL.parse(item[2]));
                    versao.setQtdCapitulos(Integer.parseInt(item[3]));
                    versao.setImagemLogotipo("");
                    versao.setElencoCompleto(false);
                    versao.setVersaoOriginal(newRegistro);
                    if (Integer.parseInt(item[4]) == 0)
                    	novelaRepository.insertVersoes(versao.getVersaoOriginal().getId(), versao.getDataLancamento(), versao.getQtdCapitulos(), versao.getImagemLogotipo(), false, versao.getAutor().getId());
                    else
                    	novelaRepository.updateVersoes(versao.getVersaoOriginal().getId(), versao.getDataLancamento(), versao.getQtdCapitulos(), versao.getImagemLogotipo(), false, versao.getAutor().getId(), Integer.parseInt(item[4]));
                }
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
                personagem.setGeneroObrig(false);
                personagem.setEtniaObrig(false);
                personagem.setFaixaEtariaObrig(false);
                personagem.setFaixaPesoObrig(false);
                personagem.setFaixaEstaturaObrig(false);
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
			newRegistro.setId(request.getId());
			newRegistro.setTitulo(request.getTitulo());
			newRegistro.setSinopse(request.getSinopse());
			novelaRepository.save(newRegistro);

            var dadosVersoes = request.getDadosVersoes();
            for (String[] item : dadosVersoes)
            {
                var versao = new VersaoNovela();
                Optional<Autor> autor = autorRepository.findByNomeArtistico(item[1]);
                if (autor.isPresent())
                {
                    versao.setAutor(autor.get());
                    versao.setDataLancamento(simpleDateFormatMySQL.parse(item[2]));
                    versao.setQtdCapitulos(Integer.parseInt(item[3]));
                    versao.setImagemLogotipo("");
                    versao.setElencoCompleto(false);
                    versao.setVersaoOriginal(newRegistro);
                    if (Integer.parseInt(item[4]) == 0)
                    	novelaRepository.insertVersoes(versao.getVersaoOriginal().getId(), versao.getDataLancamento(), versao.getQtdCapitulos(), versao.getImagemLogotipo(), false, versao.getAutor().getId());
                    else
                    	novelaRepository.updateVersoes(versao.getVersaoOriginal().getId(), versao.getDataLancamento(), versao.getQtdCapitulos(), versao.getImagemLogotipo(), false, versao.getAutor().getId(), Integer.parseInt(item[4]));
                }
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
                personagem.setGeneroObrig(false);
                personagem.setEtniaObrig(false);
                personagem.setFaixaEtariaObrig(false);
                personagem.setFaixaPesoObrig(false);
                personagem.setFaixaEstaturaObrig(false);
                if (Integer.parseInt(item[6]) == 0)
                    novelaRepository.insertPersonagens(personagem.getNovela().getId(), personagem.getNome(), 
                    								   personagem.getIdGenero(), personagem.getIdEtnia(),
                    								   personagem.getIdFaixaEtaria(), personagem.getIdFaixaPeso(), 
                    								   personagem.getIdFaixaEstatura());
                else
                	novelaRepository.updatePersonagens(personagem.getNovela().getId(), personagem.getNome(), 
                									   personagem.getIdGenero(), personagem.isGeneroObrig(), 
                									   personagem.getIdEtnia(), personagem.isEtniaObrig(), 
                									   personagem.getIdFaixaEtaria(), personagem.isFaixaEtariaObrig(), 
                									   personagem.getIdFaixaPeso(), personagem.isFaixaPesoObrig(), 
                									   personagem.getIdFaixaEstatura(), personagem.isFaixaEstaturaObrig(), 
                									   Integer.parseInt(item[6]));
            }

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
	public ResponseEntity<String> delete(@PathVariable int id)
	{
		try
		{
			Optional<Novela> novelaRepositoryAux = novelaRepository.findById(id);
			if (!novelaRepositoryAux.isPresent())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
			var registro = novelaRepositoryAux.get();
           	//novelaRepository.deleteVersoes(id);
           	//novelaRepository.deletePersonagens(id);
			var versoes = registro.getVersoes();
			var personagens = registro.getPersonagens();
			if (versoes.size() > 0 || personagens.size() > 0)
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Novela possui associação a versões ou personagens.");
			else
			{
				novelaRepository.delete(registro);
				// HTTP 200 (OK)
				return ResponseEntity.status(HttpStatus.OK).body("Dados excluídos com sucesso.");
			}
		}
		catch (IllegalArgumentException e)
		{
			// HTTP 400 (CLIENT ERROR) -> BAD REQUEST
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e)
		{
			// HTTP 500 (SERVER ERROR) -> INTERNAL SERVER ERROR
			String mensagem = e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagem);
		}
	}

	/*
	@CrossOrigin
	@ApiOperation("Endpoint para exclusão de uma versão de novela.")
	@RequestMapping(value = "/api/versaoNovela/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteVersao(@PathVariable int id)
	{
		try
		{
			versaoNovelaRepository.deleteById(null).deleteById(id);
			//novelaRepository.deleteVersao(versaoRepositoryAux.get().getId());
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
			String mensagem = e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagem);
		}
	}
	*/

	@CrossOrigin
	@ApiOperation("Endpoint para atualização de perfis de personagens.")
	@RequestMapping(value = "/api/set_prioridades_perfis", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody PersonagemRequest[] perfisPersonagens)
	{
		try
		{
            for (PersonagemRequest item : perfisPersonagens)
            {
            	novelaRepository.updatePerfisPersonagens(item.getIdNovela(), item.getNome(),
														 item.getIdGenero(), item.isGeneroObrig(),
														 item.getIdEtnia(), item.isEtniaObrig(),
														 item.getIdFaixaEtaria(), item.isFaixaEtariaObrig(),
														 item.getIdFaixaPeso(), item.isFaixaPesoObrig(),
														 item.getIdFaixaEstatura(), item.isFaixaEstaturaObrig());            		
            }

        	// HTTP 200 (OK)
			return ResponseEntity.status(HttpStatus.OK).body("Operação realizada com sucesso.");
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
}