package br.com.apiremakenovelas.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import br.com.apiremakenovelas.entities.Ator;
import br.com.apiremakenovelas.enums.Genero;
import br.com.apiremakenovelas.enums.Etnia;
import br.com.apiremakenovelas.enums.FaixaEtaria;
import br.com.apiremakenovelas.enums.FaixaPeso;
import br.com.apiremakenovelas.enums.FaixaEstatura;
import br.com.apiremakenovelas.requests.AtorRequest;
import br.com.apiremakenovelas.responses.AtorGetResponse;
import br.com.apiremakenovelas.repositories.IAtorRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class AtorController
{
	@Autowired
	private IAtorRepository repository;
	private String dataMinima = "0001-01-01";
	SimpleDateFormat simpleDateFormatBR = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat simpleDateFormatMySQL = new SimpleDateFormat("yyyy-MM-dd");
	
	@CrossOrigin
	@ApiOperation("Endpoint para consulta de atores.")
	@RequestMapping(value = "/api/ator", method = RequestMethod.GET)
	public ResponseEntity<List<AtorGetResponse>> get()
	{	
		try
		{	
			List<AtorGetResponse> lista = new ArrayList<AtorGetResponse>();

			// consultando os registros existentes no banco de dados
			for (Ator registro : repository.findAll())
			{
				AtorGetResponse response = new AtorGetResponse();
				String[] dadosCidadeNatal = registro.getCidadeNatal().split("-");
				response.setId(registro.getId());
				response.setNomeCompleto(registro.getNomeCompleto());
				response.setNomeArtistico(registro.getNomeArtistico().toUpperCase());
				response.setDataNascimento(simpleDateFormatBR.format(registro.getDataNascimento()));
				if (registro.getDataFalecimento() != null && !simpleDateFormatBR.format(registro.getDataFalecimento()).equals(dataMinima))
					response.setDataFalecimento(simpleDateFormatBR.format(registro.getDataFalecimento()));
				response.setCidadeNatal(dadosCidadeNatal[0]);
				response.setUF(dadosCidadeNatal[1]);
    			response.setImagemFoto(registro.getImagemfoto());
				response.setGeneroId(registro.getIdgenero());
				response.setEtniaId(registro.getIdetnia());
				response.setFaixaEtariaId(registro.getIdfaixaetaria());
				response.setFaixaPesoId(registro.getIdfaixapeso());
				response.setFaixaEstaturaId(registro.getIdfaixaestatura());
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
	@RequestMapping(value = "/api/ator/{id}", method = RequestMethod.GET)
	public ResponseEntity<AtorGetResponse> get(@PathVariable int id)
	{
		try
		{
			AtorGetResponse resultado = new AtorGetResponse();
			Optional<Ator> repositoryAux = repository.findById(id);
			if (repositoryAux != null)
			{
				Ator registro = repositoryAux.get();
	            String[] dadosCidadeNatal = registro.getCidadeNatal().split("-");
	            resultado.setId(id);
	            resultado.setNomeCompleto(registro.getNomeCompleto());
	            resultado.setNomeArtistico(registro.getNomeArtistico());
	            resultado.setDataNascimento(simpleDateFormatMySQL.format(registro.getDataNascimento()));
				if (registro.getDataFalecimento() != null && !simpleDateFormatMySQL.format(registro.getDataFalecimento()).equals(dataMinima))
					resultado.setDataFalecimento(simpleDateFormatMySQL.format(registro.getDataFalecimento()));
				resultado.setCidadeNatal(dadosCidadeNatal[0].trim());
				resultado.setUF(dadosCidadeNatal[1].trim());
				resultado.setImagemFoto(registro.getImagemfoto());
				resultado.setGeneroId(registro.getIdgenero());
				resultado.setEtniaId(registro.getIdetnia());
				resultado.setFaixaEtariaId(registro.getIdfaixaetaria());
				resultado.setFaixaPesoId(registro.getIdfaixapeso());
				resultado.setFaixaEstaturaId(registro.getIdfaixaestatura());
	            //String nomeArquivo = "";
                //String caminhoArquivo = "C:\\Users\\cassi\\Desktop\\SistemaRemakeNovelas - FRONT-END\\JAVA\\imagens\\atores\\";
	            //if (registro.ImagemFoto != null)
	            //{
	                //nomeArquivo = registro.getNomeArtistico().replace(" ", "_").toUpperCase().replace("'", "").toUpperCase() + ".jpg";
	                //caminhoArquivo += nomeArquivo;
	                /*if (System.IO.File.Exists(caminhoArquivo))
	                {
	                    FileStream fRead = new FileStream(caminhoArquivo, FileMode.Open);
	                    int length = fRead.ReadByte();
	                    byte[] readBytes = new byte[length];
	                    fRead.Read(readBytes, 0, readBytes.Length);
	                    foreach (var item in fRead.Name.Split("\\"))
	                        nomeArquivo = item;
	                    fRead.Close();
	                }*/
	            //}
	            String itensAtores = "";
    			for (Ator item : repository.findAll())
    				itensAtores += item.getNomeArtistico().toUpperCase() + "|";
    			String[] nomesArtisticos = (itensAtores.substring(0, itensAtores.length() - 1)).split("|");
    			resultado.setNomesArtisticos(nomesArtisticos);
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
	@ApiOperation("Endpoint para preenchimento de combos do cadastro de atores.")
	@RequestMapping(value = "/api/novela/combo/{opcao}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getItensCombo(@PathVariable int opcao)
    {
		List<String> itens = new ArrayList<String>();
        switch (opcao)
        {
            case 1:
            	Genero[] generos = Genero.values();
            	for (int i = 0; i < generos.length; i++)
            		itens.add(generos[i].getId() + "|" + generos[i].getDescricao());            		
                break;
            case 2:
            	Etnia[] etnias = Etnia.values();
            	for (int i = 0; i < etnias.length; i++)
            		itens.add(etnias[i].getId() + "|" + etnias[i].getDescricao());            		
                break;
            case 3:
            	FaixaEtaria[] faixasEtarias = FaixaEtaria.values();
            	for (int i = 0; i < faixasEtarias.length; i++)
            		itens.add(faixasEtarias[i].getId() + "|" + faixasEtarias[i].getDescricao());
                break;
            case 4:
            	FaixaPeso[] faixasPesos = FaixaPeso.values();
            	for (int i = 0; i < faixasPesos.length; i++)
            		itens.add(faixasPesos[i].getId() + "|" + faixasPesos[i].getDescricao());
                break;
            case 5:
            	FaixaEstatura[] faixasEstaturas = FaixaEstatura.values();
            	for (int i = 0; i < faixasEstaturas.length; i++)
            		itens.add(faixasEstaturas[i].getId() + "|" + faixasEstaturas[i].getDescricao());
                break;
        }
		return ResponseEntity.status(HttpStatus.OK).body(itens);
    }

	@CrossOrigin
	@ApiOperation("Endpoint para cadastro de atores.")
	@RequestMapping(value = "/api/ator", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody AtorRequest request)
	{
		try
		{
			// capturando e salvando os dados
			Ator newRegistro = new Ator();
			Date dataNascimento = simpleDateFormatMySQL.parse(request.getDataNascimento());
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento().equals("") ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemfoto(request.getImagemFoto());
			newRegistro.setIdgenero(request.getGeneroId());
			newRegistro.setIdetnia(request.getEtniaId());
			newRegistro.setIdfaixaetaria(request.getFaixaEtariaId());
			newRegistro.setIdfaixapeso(request.getFaixaPesoId());
			newRegistro.setIdfaixaestatura(request.getFaixaEstaturaId());
			repository.save(newRegistro);
			
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
	@ApiOperation("Endpoint para atualização de atores.")
	@RequestMapping(value = "/api/ator", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody AtorRequest request)
	{
		try
		{
			Optional<Ator> oldRegistro = repository.findById(request.getId());
	        if (!oldRegistro.isPresent())
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");

	        // capturando e salvando os dados
			Ator newRegistro = oldRegistro.get();
			Date dataNascimento = simpleDateFormatMySQL.parse(request.getDataNascimento());
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento().equals("") ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemfoto(request.getImagemFoto());
			newRegistro.setIdgenero(request.getGeneroId());
			newRegistro.setIdetnia(request.getEtniaId());
			newRegistro.setIdfaixaetaria(request.getFaixaEtariaId());
			newRegistro.setIdfaixapeso(request.getFaixaPesoId());
			newRegistro.setIdfaixaestatura(request.getFaixaEstaturaId());
			newRegistro.setId(request.getId());
			repository.save(newRegistro);
			
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
	@ApiOperation("Endpoint para exclusão de atores.")
	@RequestMapping(value = "/api/ator/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id)
	{
		try
		{
			Optional<Ator> registro = repository.findById(id);
			if (!registro.isPresent())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
			repository.delete(registro.get());

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