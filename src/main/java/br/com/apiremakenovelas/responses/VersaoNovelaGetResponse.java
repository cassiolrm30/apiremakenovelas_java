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
public class VersaoNovelaGetResponse
{
	private int id;
	private String dataNascimento;
	private int qtdCapitulos;
	private String imagemLogotipo;
	private int autorId;
	private int versaoOriginalId;
	private boolean elencoCompleto;
}