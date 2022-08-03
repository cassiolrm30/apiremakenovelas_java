package br.com.apiremakenovelas.enums;

public enum Genero
{
	Feminino(1, "Feminino"), Masculino(2, "Masculino"), Ambos(3, "Ambos");

	private final int id;
	private final String descricao;

	Genero(int id, String descricao)
	{
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() { return id; }

	public String getDescricao() { return descricao; }
}