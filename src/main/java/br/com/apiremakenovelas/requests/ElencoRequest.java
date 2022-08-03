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
public class ElencoRequest
{
	int novelaId;
	int versaoNovelaId;
	int[] interpretacoesIds;
	boolean completo;
}