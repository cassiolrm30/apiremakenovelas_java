package br.com.apiremakenovelas.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "versaonovela")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VersaoNovela
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "datalancamento", nullable = false)
	private Date dataLancamento;

	@Column(name = "qtdcapitulos", nullable = false)
	private Integer qtdcapitulos;
	
	@Column(name = "imagemlogotipo", nullable = false)
	private String imagemlogotipo;

	@Column(name = "elencocompleto", nullable = false)
	private boolean elencoCompleto;

	@ManyToOne
	@JoinColumn(name = "idautor", nullable = false)
	private Autor autor;

	@ManyToOne
	@JoinColumn(name = "idversaooriginal", nullable = true)
	private Novela versaoOriginal;
}