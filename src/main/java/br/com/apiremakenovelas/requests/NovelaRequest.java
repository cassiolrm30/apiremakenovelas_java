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
public class NovelaRequest
{
	private int id;
	private String titulo;
	private String sinopse;
	private String autorOriginal;
	private String autorAtual;
	private String[][] dadosVersoes;
	private String[][] dadosPersonagens;
}