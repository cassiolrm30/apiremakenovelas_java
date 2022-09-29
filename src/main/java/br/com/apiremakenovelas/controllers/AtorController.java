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
import br.com.apiremakenovelas.requests.AtorImportRequest;
import br.com.apiremakenovelas.requests.AtorRequest;
import br.com.apiremakenovelas.responses.AtorGetResponse;
import br.com.apiremakenovelas.responses.ImportacaoGetResponse;
import br.com.apiremakenovelas.repositories.IAtorRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class AtorController
{
	@Autowired
	private IAtorRepository repository;
	private final String dataMinima = "0001-01-01";
	private final SimpleDateFormat simpleDateFormatBR = new SimpleDateFormat("dd/MM/yyyy");
	private final SimpleDateFormat simpleDateFormatMySQL = new SimpleDateFormat("yyyy-MM-dd");
    private final String separador = "|";
	
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
				String nomeArquivo = registro.getNomeArtistico();
				response.setId(registro.getId());
				response.setNomeCompleto(registro.getNomeCompleto());
				response.setNomeArtistico(registro.getNomeArtistico().toUpperCase());
				response.setDataNascimento(simpleDateFormatBR.format(registro.getDataNascimento()));
				if (registro.getDataFalecimento() != null && !simpleDateFormatBR.format(registro.getDataFalecimento()).equals(dataMinima))
					response.setDataFalecimento(simpleDateFormatBR.format(registro.getDataFalecimento()));
				response.setCidadeNatal(dadosCidadeNatal[0]);
				response.setUF(dadosCidadeNatal[1]);
				response.setIdGenero(registro.getIdGenero());
				response.setIdEtnia(registro.getIdEtnia());
				response.setIdFaixaEtaria(registro.getIdFaixaEtaria());
				response.setIdFaixaPeso(registro.getIdFaixaPeso());
				response.setIdFaixaEstatura(registro.getIdFaixaEstatura());
				response.setImagemFoto(getCaminhoArquivo(nomeArquivo));
				response.setImagemUpload(getCaminhoArquivo(nomeArquivo));
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
	            String nomeArquivo = registro.getNomeArtistico();
	            resultado.setId(id);
	            resultado.setNomeCompleto(registro.getNomeCompleto());
	            resultado.setNomeArtistico(registro.getNomeArtistico());
	            resultado.setDataNascimento(simpleDateFormatMySQL.format(registro.getDataNascimento()));
				if (registro.getDataFalecimento() != null && !simpleDateFormatMySQL.format(registro.getDataFalecimento()).equals(dataMinima))
					resultado.setDataFalecimento(simpleDateFormatMySQL.format(registro.getDataFalecimento()));
				resultado.setCidadeNatal(dadosCidadeNatal[0].trim());
				resultado.setUF(dadosCidadeNatal[1].trim());
				resultado.setIdGenero(registro.getIdGenero());
				resultado.setIdEtnia(registro.getIdEtnia());
				resultado.setIdFaixaEtaria(registro.getIdFaixaEtaria());
				resultado.setIdFaixaPeso(registro.getIdFaixaPeso());
				resultado.setIdFaixaEstatura(registro.getIdFaixaEstatura());
				resultado.setImagemFoto(getCaminhoArquivo(nomeArquivo));
				resultado.setImagemUpload(getCaminhoArquivo(nomeArquivo));
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

	public String getCaminhoArquivo(@PathVariable String nomeArquivo)
    {
        String resultado = "";
        //String caminhoArquivo = "C:\\Users\\cassi\\Desktop\\SistemaRemakeNovelas - FRONT-END\\JAVA\\imagens\\atores\\";
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
	@ApiOperation("Endpoint para cadastro de atores.")
	@RequestMapping(value = "/api/ator", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody AtorRequest request)
	{
		try
		{
			// capturando e salvando os dados
			Ator newRegistro = new Ator();
			Date dataNascimento = simpleDateFormatMySQL.parse(request.getDataNascimento());
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento() == null ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemFoto(request.getImagemFoto());
			newRegistro.setIdGenero(request.getIdGenero());
			newRegistro.setIdEtnia(request.getIdEtnia());
			newRegistro.setIdFaixaEtaria(request.getIdFaixaEtaria());
			newRegistro.setIdFaixaPeso(request.getIdFaixaPeso());
			newRegistro.setIdFaixaEstatura(request.getIdFaixaEstatura());
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
			Date dataFalecimento = simpleDateFormatMySQL.parse((request.getDataFalecimento() == null ? dataMinima : request.getDataFalecimento()));
			newRegistro.setNomeCompleto(request.getNomeCompleto());
			newRegistro.setNomeArtistico(request.getNomeArtistico());
			newRegistro.setDataNascimento(dataNascimento);
			newRegistro.setDataFalecimento(dataFalecimento);
			newRegistro.setCidadeNatal(request.getCidadeNatal() + "-" + request.getUF());
			newRegistro.setImagemFoto(request.getImagemFoto());
			newRegistro.setIdGenero(request.getIdGenero());
			newRegistro.setIdEtnia(request.getIdEtnia());
			newRegistro.setIdFaixaEtaria(request.getIdFaixaEtaria());
			newRegistro.setIdFaixaPeso(request.getIdFaixaPeso());
			newRegistro.setIdFaixaEstatura(request.getIdFaixaEstatura());
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

	@CrossOrigin
	@ApiOperation("Endpoint para importação de dados de atores.")
	@RequestMapping(value = "/api/ator/importar", method = RequestMethod.PUT)
	public ResponseEntity<ImportacaoGetResponse> importar(@RequestBody AtorImportRequest[] dadosImportacao)
	{
		ImportacaoGetResponse resultado = new ImportacaoGetResponse();
		int qtdInserts = 0, qtdUpdates = 0;
		try
		{
			for (AtorImportRequest item : dadosImportacao)
			{
				if ((item.getNomeArtistico() != "" && item.getNomeArtistico() != null) && (item.getDataNascimento() != "" && item.getDataNascimento() != null))
				{
					String[] perfil = (item.getPerfil().replace(separador, " ")).split(" ");
					Optional<Ator> oldRegistro = repository.findByNomeArtistico(item.getNomeArtistico());
		        	Ator newRegistro = new Ator();
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
					newRegistro.setIdGenero(Integer.parseInt(perfil[0]));
					newRegistro.setIdEtnia(Integer.parseInt(perfil[1]));
					newRegistro.setIdFaixaEtaria(Integer.parseInt(perfil[2]));
					newRegistro.setIdFaixaPeso(Integer.parseInt(perfil[3]));
					newRegistro.setIdFaixaEstatura(Integer.parseInt(perfil[4]));
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