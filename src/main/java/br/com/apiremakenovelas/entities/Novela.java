package br.com.apiremakenovelas.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "novela")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Novela
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "titulo", length = 50, nullable = false)
	private String titulo;

	@Column(name = "sinopse", length = 4000, nullable = false)
	private String sinopse;

	@OneToMany(mappedBy = "novela")
	private List<Personagem> personagens;

	@OneToMany(mappedBy = "versaoOriginal")
	private List<VersaoNovela> versoes;
}