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
public class AutorImportRequest
{
	private String nomeArtistico;
	private String nomeCompleto;
	private String dataNascimento;
	private String dataFalecimento;
	private int idade, idGenero;
	private String cidadeNatal;
	private String UF;
}