package br.com.apiremakenovelas.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonagemRequest
{
	private int id;
	private String nome;
	private int idGenero, idEtnia, idFaixaEtaria, idFaixaPeso, idFaixaEstatura, idNovela;
	private boolean generoObrig, etniaObrig, faixaEtariaObrig, faixaPesoObrig, faixaEstaturaObrig;
	private int[] atoresIds;
}