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
public class VersaoNovelaRequest
{
	private int id;
	private String dataNascimento;
	private int qtdCapitulos;
	private String imagemLogotipo;
	private int idAutor;
	private int idVersaoOriginal;
	private boolean elencoCompleto;
}