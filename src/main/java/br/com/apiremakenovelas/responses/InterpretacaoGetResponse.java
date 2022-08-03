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
public class InterpretacaoGetResponse
{
	private int id;
	private int personagemId;
	private int versaoNovelaId;
	private int atorId;
}
