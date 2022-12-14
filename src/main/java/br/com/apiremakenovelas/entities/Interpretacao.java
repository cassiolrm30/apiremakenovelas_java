package br.com.apiremakenovelas.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "interpretacao")
@Setter
@Getter
public class Interpretacao
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_ator", nullable = false)
	private Ator ator;

	@ManyToOne
	@JoinColumn(name = "id_versao_novela", nullable = false)
	private VersaoNovela versaoNovela;

	@ManyToOne
	@JoinColumn(name = "id_personagem", nullable = false)
	private Personagem personagem;

	public Interpretacao()
	{
		this.setVersaoNovela(new VersaoNovela());
		this.setPersonagem(new Personagem());
		this.setAtor(new Ator());
	}
}