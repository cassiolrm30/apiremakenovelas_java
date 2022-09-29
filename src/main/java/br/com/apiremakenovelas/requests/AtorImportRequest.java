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
public class AtorImportRequest
{
	private String nomeArtistico;
	private String nomeCompleto;
	private String dataNascimento;
	private String dataFalecimento;
	private String UF;
	private String cidadeNatal;
	private String perfil; //	"1|2|1|10|8"
}