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
public class AtorGetResponse
{
	private int id;
	private int generoId, etniaId, faixaEtariaId, faixaPesoId, faixaEstaturaId;
	private String nomeCompleto;
	private String nomeArtistico;
	private String dataNascimento;
	private String dataFalecimento;
	private String UF;
	private String cidadeNatal;
	private String imagemUpload;
	private String imagemFoto;
	private String[] nomesArtisticos;
	private String perfil;
}