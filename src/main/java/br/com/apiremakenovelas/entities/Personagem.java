package br.com.apiremakenovelas.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "personagem")
@Setter
@Getter
@AllArgsConstructor
@ToString
public class Personagem
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nome", length = 50, nullable = false)
	private String nome;

	@Column(name = "idgenero", nullable = false)
	private Integer idGenero;

	@Column(name = "generoobrig", nullable = false)
	private boolean generoObrig;

	@Column(name = "idetnia", nullable = false)
	private Integer idEtnia;
	
	@Column(name = "etniaobrig", nullable = false)
	private boolean etniaObrig;
	
	@Column(name = "idfaixaetaria", nullable = false)
	private Integer idFaixaEtaria;
	
	@Column(name = "faixaetariaobrig", nullable = false)
	private boolean faixaEtariaObrig;

	@Column(name = "idfaixapeso", nullable = false)
	private Integer idFaixaPeso;
	
	@Column(name = "faixapesoobrig", nullable = false)
	private boolean faixaPesoObrig;
	
	@Column(name = "idfaixaestatura", nullable = false)
	private Integer idFaixaEstatura;

	@Column(name = "faixaestaturaobrig", nullable = false)
	private boolean faixaEstaturaObrig;

	@ManyToOne
	@JoinColumn(name = "idnovela", nullable = false)
	private Novela novela;
	
	public Personagem()
	{
		this.setNovela(new Novela());
	}
}