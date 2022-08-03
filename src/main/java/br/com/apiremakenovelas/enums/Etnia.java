package br.com.apiremakenovelas.enums;

public enum Etnia
{
	ASIATICO(1, "Asiático(a)"), BRANCO(2, "Branco(a)"), INDIGENA(3, "Indígena"), NEGRO(4, "Negro(a)");

	private final int id;
	private final String descricao;

	Etnia(int id, String descricao)
	{
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() { return id; }

	public String getDescricao() { return descricao; }
}