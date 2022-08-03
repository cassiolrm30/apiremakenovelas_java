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
public class InterpretacaoRequest
{
	private int id;
	private int personagemId;
	private int versaoNovelaId;
	private int atorId;
}