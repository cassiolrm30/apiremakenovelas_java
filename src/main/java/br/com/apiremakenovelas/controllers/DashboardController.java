package br.com.apiremakenovelas.controllers;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import br.com.apiremakenovelas.enums.Etnia;
import br.com.apiremakenovelas.enums.FaixaEstatura;
import br.com.apiremakenovelas.enums.FaixaEtaria;
import br.com.apiremakenovelas.enums.FaixaPeso;
import br.com.apiremakenovelas.enums.Genero;
import br.com.apiremakenovelas.repositories.IAtorRepository;
import br.com.apiremakenovelas.repositories.IAutorRepository;
import io.swagger.annotations.ApiOperation;

@Transactional
@Controller
public class DashboardController
{
	@Autowired
	private IAtorRepository atorRepository;

	@Autowired
	private IAutorRepository autorRepository;
    
	private final String separador = "|";

    @CrossOrigin
	@ApiOperation("Endpoint para dados de dashboards.")
	@RequestMapping(value = "/api/dashboard", method = RequestMethod.GET)
	public ResponseEntity<String[]> getItensDashboardProfissionais()
	{	
		String[] resultado = new String[5];
		try
		{
			List<String> valoresPorGrupo;
			String conteudo;

			var generos = Genero.values();
			var atoresPorGrupo = atorRepository.countByGenero();
			var autoresPorGrupo = autorRepository.countByGenero();
			valoresPorGrupo = new ArrayList();
			for (var registro1 : atoresPorGrupo)
			{
				var valoresRegistro1 = registro1.split(";");
				int valorCount = Integer.parseInt(valoresRegistro1[1]);
				for (var registro2 : autoresPorGrupo)
				{
					var valoresRegistro2 = registro2.split(";");
					if (valoresRegistro2[0].equals(valoresRegistro1[0]))
					{
						valorCount += Integer.parseInt(valoresRegistro2[1]);
						break;
					}
				}
				valoresPorGrupo.add(valoresRegistro1[0] + ";" + valorCount);
			}
			conteudo = separador;
			for (var registro : valoresPorGrupo)
			{
				conteudo += separador;
				for (var item : generos)
				{
					var dados = registro.split(";");
					if (item.getId() == Integer.parseInt(dados[0]))
					{
						conteudo += item.getDescricao() + ";" + dados[1];
						break;
					}
				}
			}
			resultado[0] = conteudo.replace(separador + separador, "");

			var etnias = Etnia.values();
			valoresPorGrupo = atorRepository.countByEtnia();
			conteudo = separador;
			for (var registro : valoresPorGrupo)
			{
				conteudo += separador;
				for (var item : etnias)
				{
					var dados = registro.split(";");
					if (item.getId() == Integer.parseInt(dados[0]))
					{
						conteudo += item.getDescricao() + ";" + dados[1];
						break;
					}
				}
			}
			resultado[1] = conteudo.replace(separador + separador, "");

			var faixasEtarias = FaixaEtaria.values();
			valoresPorGrupo = atorRepository.countByFaixaEtaria();
			conteudo = separador;
			for (var registro : valoresPorGrupo)
			{
				conteudo += separador;
				for (var item : faixasEtarias)
				{
					var dados = registro.split(";");
					if (item.getId() == Integer.parseInt(dados[0]))
					{
						conteudo += item.getDescricao() + ";" + dados[1];
						break;
					}
				}
			}
			resultado[2] = conteudo.replace(separador + separador, "");

			var faixasPesos = FaixaPeso.values();
			valoresPorGrupo = atorRepository.countByFaixaPeso();
			conteudo = separador;
			for (var registro : valoresPorGrupo)
			{
				conteudo += separador;
				for (var item : faixasPesos)
				{
					var dados = registro.split(";");
					if (item.getId() == Integer.parseInt(dados[0]))
					{
						conteudo += item.getDescricao() + ";" + dados[1];
						break;
					}
				}
			}
			resultado[3] = conteudo.replace(separador + separador, "");

			var faixasEstaturas = FaixaEstatura.values();
			valoresPorGrupo = atorRepository.countByFaixaEstatura();
			conteudo = separador;
			for (var registro : valoresPorGrupo)
			{
				conteudo += separador;
				for (var item : faixasEstaturas)
				{
					var dados = registro.split(";");
					if (item.getId() == Integer.parseInt(dados[0]))
					{
						conteudo += item.getDescricao() + ";" + dados[1];
						break;
					}
				}
			}
			resultado[4] = conteudo.replace(separador + separador, "");

			// HTTP 200 (OK)
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
		}
		catch(Exception e)
		{
			// HTTP 500 (INTERNAL SERVER ERROR)
			resultado[0] = e.getMessage();
			resultado[1] = e.getLocalizedMessage();
			resultado[2] = null;
			resultado[3] = null;
			resultado[4] = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
		}
	}
}