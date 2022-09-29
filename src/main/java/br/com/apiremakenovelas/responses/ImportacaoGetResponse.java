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
public class ImportacaoGetResponse
{
	private String mensagem;
	private int qtdRegistros;
	private int qtdCadastros;
	private int qtdAtualizacoes;
}
