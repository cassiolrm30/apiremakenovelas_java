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
public class VersaoNovelaController
{
	@Autowired
	private IVersaoNovelaRepository repository;

	@CrossOrigin
	@ApiOperation("Endpoint para exclusão de versões de novelas.")
	@RequestMapping(value = "/api/versaoNovela/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Integer id)
	{
		try
		{
			repository.deleteById(id);
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
}