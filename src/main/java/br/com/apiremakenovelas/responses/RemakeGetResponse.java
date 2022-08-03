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
public class RemakeGetResponse
{
	private String[] dadosPersonagens;
	private String[] dadosVersoes; 
	private String[] dadosAtores; 
	private String tituloNovela;
	int idVersaoAtual;
}