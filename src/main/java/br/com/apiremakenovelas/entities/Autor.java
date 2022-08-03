package br.com.apiremakenovelas.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "autor")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Autor
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nomecompleto", length = 50, nullable = false)
	private String nomeCompleto;
	
	@Column(name = "nomeartistico", length = 50, nullable = false)
	private String nomeArtistico;

	@Temporal(TemporalType.DATE)
	@Column(name = "datanascimento", nullable = false)
	private Date dataNascimento;

	@Temporal(TemporalType.DATE)
	@Column(name = "datafalecimento", nullable = true)
	private Date dataFalecimento;
	
	@Column(name = "cidadenatal", length = 30, nullable = false)
	private String cidadeNatal;
	
	@Column(name = "imagemfoto", nullable = true)
	private String imagemfoto;

	@JoinColumn(name = "idgenero", nullable = false)
	private Integer idgenero;

	@OneToMany(mappedBy = "autor")
	private List<VersaoNovela> novelas;	
}