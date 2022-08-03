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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "personagem")
@Setter
@Getter
@NoArgsConstructor
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
	private Integer idgenero;

	@Column(name = "generoobrig", nullable = false)
	private boolean generoobrig;

	@Column(name = "idetnia", nullable = false)
	private Integer idetnia;
	
	@Column(name = "etniaobrig", nullable = false)
	private boolean etniaobrig;
	
	@Column(name = "idfaixaetaria", nullable = false)
	private Integer idfaixaetaria;
	
	@Column(name = "faixaetariaobrig", nullable = false)
	private boolean faixaetariaobrig;

	@Column(name = "idfaixapeso", nullable = false)
	private Integer idfaixapeso;
	
	@Column(name = "faixapesoobrig", nullable = false)
	private boolean faixapesoobrig;
	
	@Column(name = "idfaixaestatura", nullable = false)
	private Integer idfaixaestatura;

	@Column(name = "faixaestaturaobrig", nullable = false)
	private boolean faixaestaturaobrig;

	@ManyToOne
	@JoinColumn(name = "idnovela", nullable = false)
	private Novela novela;
}