package br.com.apiremakenovelas.controllers;

import java.util.ArrayList;
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
import br.com.apiremakenovelas.entities.Personagem;
import br.com.apiremakenovelas.requests.PersonagemRequest;
import br.com.apiremakenovelas.responses.PersonagemGetResponse;
import br.com.apiremakenovelas.repositories.IPersonagemRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class PersonagemController
{
	@Autowired
	private IPersonagemRepository repository;

	@CrossOrigin
	@ApiOperation("Endpoint para consulta de personagens.")
	@RequestMapping(value = "/api/personagem", method = RequestMethod.GET)
	public ResponseEntity<List<PersonagemGetResponse>> get()
	{	
		try
		{	
			List<PersonagemGetResponse> lista = new ArrayList<PersonagemGetResponse>();

			// consultando os registros existentes no banco de dados
			for (Personagem registro : repository.findAll())
			{
				PersonagemGetResponse response = new PersonagemGetResponse();
				response.setId(registro.getId());
				response.setNome(registro.getNome());
				response.setIdGenero(registro.getIdGenero());
				response.setIdEtnia(registro.getIdEtnia());
				response.setIdFaixaEtaria(registro.getIdFaixaEtaria());
				response.setIdFaixaPeso(registro.getIdFaixaPeso());
				response.setIdFaixaEstatura(registro.getIdFaixaEstatura());
				response.setGeneroObrig(registro.isGeneroObrig());
				response.setEtniaObrig(registro.isEtniaObrig());
				response.setFaixaEtariaObrig(registro.isFaixaEtariaObrig());
				response.setFaixaPesoObrig(registro.isFaixaPesoObrig());
				response.setFaixaEstaturaObrig(registro.isFaixaEstaturaObrig());
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
	@RequestMapping(value = "/api/personagem/{id}", method = RequestMethod.GET)
	public ResponseEntity<PersonagemGetResponse> get(@PathVariable int id)
	{
		try
		{
			PersonagemGetResponse resultado = new PersonagemGetResponse();
			Optional<Personagem> repositoryAux = repository.findById(id);
			if (repositoryAux != null)
			{
				Personagem registro = repositoryAux.get();
				PersonagemGetResponse response = new PersonagemGetResponse();
				response.setId(id);
				response.setNome(registro.getNome());
				response.setIdGenero(registro.getIdGenero());
				response.setIdEtnia(registro.getIdEtnia());
				response.setIdFaixaEtaria(registro.getIdFaixaEtaria());
				response.setIdFaixaPeso(registro.getIdFaixaPeso());
				response.setIdFaixaEstatura(registro.getIdFaixaEstatura());
				response.setGeneroObrig(registro.isGeneroObrig());
				response.setEtniaObrig(registro.isEtniaObrig());
				response.setFaixaEtariaObrig(registro.isFaixaEtariaObrig());
				response.setFaixaPesoObrig(registro.isFaixaPesoObrig());
				response.setFaixaEstaturaObrig(registro.isFaixaEstaturaObrig());
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
	@ApiOperation("Endpoint para cadastro de personagens.")
	@RequestMapping(value = "/api/personagem", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody PersonagemRequest request)
	{
		try
		{
			// capturando e salvando os dados
			Personagem newRegistro = new Personagem();
			newRegistro.setNome(request.getNome());
			newRegistro.setIdGenero(null);
			newRegistro.setIdEtnia(null);
			newRegistro.setIdFaixaEtaria(null);
			newRegistro.setIdFaixaPeso(null);
			newRegistro.setIdFaixaEstatura(null);
			newRegistro.setGeneroObrig(false);
			newRegistro.setEtniaObrig(false);
			newRegistro.setFaixaEtariaObrig(false);
			newRegistro.setFaixaPesoObrig(false);
			newRegistro.setFaixaEstaturaObrig(false);
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
	@ApiOperation("Endpoint para atualização de personagens.")
	@RequestMapping(value = "/api/personagem", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody PersonagemRequest request)
	{
		try
		{
			Optional<Personagem> oldRegistro = repository.findById(request.getId());
	        if (!oldRegistro.isPresent())
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");

	        // capturando e salvando os dados
	        Personagem newRegistro = oldRegistro.get();
			newRegistro.setIdGenero(null);
			newRegistro.setIdEtnia(null);
			newRegistro.setIdFaixaEtaria(null);
			newRegistro.setIdFaixaPeso(null);
			newRegistro.setIdFaixaEstatura(null);
			newRegistro.setGeneroObrig(false);
			newRegistro.setEtniaObrig(false);
			newRegistro.setFaixaEtariaObrig(false);
			newRegistro.setFaixaPesoObrig(false);
			newRegistro.setFaixaEstaturaObrig(false);
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
	@ApiOperation("Endpoint para exclusão de personagens.")
	@RequestMapping(value = "/api/personagem/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id)
	{
		try
		{
			Optional<Personagem> registro = repository.findById(id);
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