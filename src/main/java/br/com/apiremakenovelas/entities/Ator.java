package br.com.apiremakenovelas.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Entity
@Table(name = "ator")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ator
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

	@JoinColumn(name = "id_genero", nullable = false)
	private Integer idGenero;

	@JoinColumn(name = "id_etnia", nullable = false)
	private Integer idEtnia;

	@JoinColumn(name = "id_faixa_etaria", nullable = false)
	private Integer idFaixaEtaria;
	
	@JoinColumn(name = "id_faixa_peso", nullable = false)
	private Integer idFaixaPeso;

	@JoinColumn(name = "id_faixa_estatura", nullable = false)
	private Integer idFaixaEstatura;
	
	@Column(name = "imagemfoto", nullable = true)
	private String imagemFoto;
}