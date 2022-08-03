package br.com.apiremakenovelas.responses;

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
public class PersonagemGetResponse
{
	private int id;
	private String nome;
	private int Idgenero, Idetnia, Idfaixaetaria, Idfaixapeso, Idfaixaestatura, Idnovela;
	private boolean generoObrig, etniaObrig, faixaEtariaObrig, faixaPesoObrig, faixaEstaturaObrig;
	private int[] atoresIds;
}