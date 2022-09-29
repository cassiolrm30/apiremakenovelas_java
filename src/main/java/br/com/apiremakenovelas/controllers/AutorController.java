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
import br.com.apiremakenovelas.entities.Autor;
import br.com.apiremakenovelas.entities.Novela;
import br.com.apiremakenovelas.entities.VersaoNovela;
import br.com.apiremakenovelas.requests.AtorImportRequest;
import br.com.apiremakenovelas.requests.AutorImportRequest;
import br.com.apiremakenovelas.requests.AutorRequest;
import br.com.apiremakenovelas.responses.AutorGetResponse;
import br.com.apiremakenovelas.responses.ImportacaoGetResponse;
import br.com.apiremakenovelas.repositories.IAutorRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class AutorController
{
	@Autowired
	private IAutorRepository repository;
	private String dataMinima = "0001-01-01";
	SimpleDateFormat simpleDateFormatBR = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat simpleDateFormatMySQL = new SimpleDateFormat("yyyy-MM-dd");

	@CrossOrigin
	@ApiOperation("Endpoint para consulta de autores.")
	@RequestMapping(value = "/api/autor", method = RequestMethod.GET)
	public ResponseEntity<List<AutorGetResponse>> get()
	{	
		try
		{
			List<AutorGetResponse> lista = new ArrayList<AutorGetResponse>();

			// consultando os registros existentes no banco de dados
			for (Autor registro : repository.findAll())
			{
				AutorGetResponse response = new AutorGetResponse();
				String[] dadosCidadeNatal = registro.getCidadeNatal().split("-");
				response.setId(registro.getId());
				response.setNomeCompleto(registro.getNomeCompleto());
				response.setNomeArtistico(registro.getNomeArtistico().toUpperCase());
				response.setDataNascimento(simpleDateFormatBR.format(registro.getDataNascimento()));
				response.setCidadeNatal(dadosCidadeNatal[0]);
				response.setUF(dadosCidadeNatal[1]);
				response.setImagemFoto(getCaminhoArquivo(registro.getImagemFoto()));
				response.setIdGenero(registro.getIdGenero());
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
	@RequestMapping(value = "/api/autor/{id}", method = RequestMethod.GET)
	public ResponseEntity<AutorGetResponse> get(@PathVariable int id)
	{
		try
		{
			AutorGetResponse resultado = new AutorGetResponse();
			Optional<Autor> repositoryAux = repository.findById(id);
			if (repositoryAux != null)
			{
				Autor registro = repositoryAux.get();
	            String[] dadosCidadeNatal = registro.getCidadeNatal().split("-");
	            resultado.setId(id);
	            resultado.setNomeCompleto(registro.getNomeCompleto());
	            resultado.setNomeArtistico(registro.getNomeArtistico());
	            resultado.setDataNascimento(simpleDateFormatMySQL.format(registro.getDataNascimento()));
				resultado.setCidadeNatal(dadosCidadeNatal[0].trim());
				resultado.setUF(dadosCidadeNatal[1].trim());
				resultado.setImagemFoto(getCaminhoArquivo(registro.getImagemFoto()));
				resultado.setIdGenero(registro.getIdGenero());
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
				var listaOutrosAutores = repository.findAll();
    			String[] nomesArtisticos = new String[listaOutrosAutores.size()];
    			int indice;
    			indice = 0;
    			for (Autor item : listaOutrosAutores)
    			{
    				if (!item.getNomeArtistico().toUpperCase().equals(registro.getNomeArtistico().toUpperCase()))
    					nomesArtisticos[indice] = item.getNomeArtistico().toUpperCase();
    				indice++;
    			}
    			resultado.setNomesArtisticos(nomesArtisticos);

    			var listaNovelasDoAutor = repository.findNovelas(id);
    			String[] dadosVersoesNovelas = new String[listaNovelasDoAutor.size()];
    			indice = 0;
    			for (String item : listaNovelasDoAutor)
    			{
    				dadosVersoesNovelas[indice] = item;
    				indice++;
    			}
    			resultado.setVersoesNovelas(dadosVersoesNovelas);
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

	public String getCaminhoArquivo(@PathVariable String nomeArquivo)
    {
        String resultado = "";
        //String caminhoArquivo = "C:\\Users\\cassi\\Desktop\\SistemaRemakeNovelas - FRONT-END\\JAVA\\imagens\\autores\\";
        //if (registro.ImagemFoto != null)
        //{
            //resultado = registro.getNomeArtistico().replace(" ", "_").toUpperCase().replace("'", "").toUpperCase() + ".jpg";
            //caminhoArquivo += resultado;
            /*if (System.IO.File.Exists(caminhoArquivo))
            {
                FileStream fRead = new FileStream(caminhoArquivo, FileMode.Open);
                int length = fRead.ReadByte();
                byte[] readBytes = new byte[length];
                fRead.Read(readBytes, 0, readBytes.Length);
                foreach (var item in fRead.Name.Split("\\"))
                    resultado = item;
                fRead.Close();
            }*/
        //}
		return resultado;
    }

	@CrossOrigin
	@ApiOperation("Endpoint para cadastro de autores.")
	@RequestMapping(value = "/api/autor", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody AutorRequest request)
	{
		try
		{
			// capturando e salvando os dados
			Autor newRegistro = new Autor();
			Date dataNascimento = simpleDateFormatMySQL.parse(request.getDataNascimento());
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento() == null ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemFoto(request.getImagemFoto());
			newRegistro.setIdGenero(request.getIdGenero());
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
	@ApiOperation("Endpoint para atualização de autores.")
	@RequestMapping(value = "/api/autor", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody AutorRequest request)
	{
		try
		{
			Optional<Autor> oldRegistro = repository.findById(request.getId());
	        if (!oldRegistro.isPresent())
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");

	        // capturando e salvando os dados
			Autor newRegistro = oldRegistro.get();
			Date dataNascimento = simpleDateFormatMySQL.parse(request.getDataNascimento());
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento() == null ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemFoto(request.getImagemFoto());
			newRegistro.setIdGenero(request.getIdGenero());
			newRegistro.setId(request.getId());
			repository.save(newRegistro);

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
	@ApiOperation("Endpoint para exclusão de autores.")
	@RequestMapping(value = "/api/autor/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id)
	{
		try
		{
			Optional<Autor> registro = repository.findById(id);
			if (!registro.isPresent())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
			//int qtNovelas = registro.get().getVersoesNovelas().size();
			//if (qtNovelas > 0)
			//	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor possui associação a novelas.");
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
			String mensagem = e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagem);
		}
	}

	@CrossOrigin
	@ApiOperation("Endpoint para importação de dados de atores.")
	@RequestMapping(value = "/api/autor/importar", method = RequestMethod.PUT)
	public ResponseEntity<ImportacaoGetResponse> importar(@RequestBody AutorImportRequest[] dadosImportacao)
	{
		ImportacaoGetResponse resultado = new ImportacaoGetResponse();
		int qtdInserts = 0, qtdUpdates = 0;
		try
		{
			for (AutorImportRequest item : dadosImportacao)
			{
				if ((item.getNomeArtistico() != "" && item.getNomeArtistico() != null) && (item.getDataNascimento() != "" && item.getDataNascimento() != null))
				{
		        	Autor newRegistro = new Autor();
					Optional<Autor> oldRegistro = repository.findByNomeArtistico(item.getNomeArtistico());
					if (!oldRegistro.isPresent())
					{
						newRegistro.setId(0);
						qtdInserts++;
					}
					else
					{
						newRegistro = oldRegistro.get();
						newRegistro.setId(oldRegistro.get().getId());
						qtdUpdates++;
					}
					Date dataNascimento = simpleDateFormatMySQL.parse(item.getDataNascimento());
					Date dataFalecimento = simpleDateFormatMySQL.parse((item.getDataFalecimento() == null ? dataMinima : item.getDataFalecimento()));
					newRegistro.setNomeCompleto(item.getNomeCompleto().trim().equals("") ? item.getNomeArtistico().trim() : item.getNomeCompleto().trim());
					newRegistro.setNomeArtistico(item.getNomeArtistico().trim());
					newRegistro.setDataNascimento(dataNascimento);
					newRegistro.setDataFalecimento(dataFalecimento);
					newRegistro.setCidadeNatal((item.getCidadeNatal() + "-" + item.getUF()).toUpperCase());
					newRegistro.setIdGenero(item.getIdGenero());
					repository.save(newRegistro);
				}
			}

			// HTTP 201 (CREATED)
			resultado.setMensagem("Dados importados com sucesso.");
			resultado.setQtdRegistros(dadosImportacao.length);
			resultado.setQtdCadastros(qtdInserts);
			resultado.setQtdAtualizacoes(qtdUpdates);
			return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
		}
		catch(IllegalArgumentException e)
		{
			// HTTP 400 (CLIENT ERROR) -> BAD REQUEST
			resultado.setMensagem(e.getMessage());
			resultado.setQtdRegistros(0);
			resultado.setQtdCadastros(0);
			resultado.setQtdAtualizacoes(0);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
		}
		catch(Exception e)
		{			
			// HTTP 500 (SERVER ERROR) -> INTERNAL SERVER ERROR
			resultado.setMensagem(e.getMessage());
			resultado.setQtdRegistros(0);
			resultado.setQtdCadastros(0);
			resultado.setQtdAtualizacoes(0);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
		}
	}
}