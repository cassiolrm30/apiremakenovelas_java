package br.com.apiremakenovelas.entities;
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
public class ValorPorGrupo
{
	private Integer idGrupo;

	private Integer quantidade;

	ValorPorGrupo(int idGrupo, int quantidade)
	{
		this.idGrupo = idGrupo;
		this.quantidade = quantidade;
	}
}
