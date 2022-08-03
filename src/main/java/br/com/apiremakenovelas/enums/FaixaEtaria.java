package br.com.apiremakenovelas.enums;

public enum FaixaEtaria
{
	Abaixo_de_10_Anos(1, Integer.MIN_VALUE, 10), 
	De_11_a_20_Anos(2, 11, 20),
	De_21_a_30_Anos(3, 21, 30),
	De_31_a_40_Anos(4, 31, 40),
	De_41_a_50_Anos(5, 41, 50),
	De_51_a_60_Anos(6, 51, 60),
	De_61_a_70_Anos(7, 61, 70),
	De_71_a_80_Anos(8, 71, 80),
	De_81_a_90_Anos(9, 81, 90),
	Acima_de_91_Anos(10, 91, Integer.MAX_VALUE);

	private final int id, idadeMinima, idadeMaxima;

	FaixaEtaria(int id, int idadeMinima, int idadeMaxima)
	{
		this.id = id;
		this.idadeMinima = idadeMinima;
		this.idadeMaxima = idadeMaxima;
	}

	public int getId() { return id; }

	public int getValorMinimo() { return idadeMinima; }

	public int getValorMaximo() { return idadeMaxima; }

	public String getDescricao()
	{
		Integer _valorMinimo = this.getValorMinimo();
		Integer _valorMaximo = this.getValorMaximo();
		if (_valorMinimo == Integer.MIN_VALUE) return "Abaixo de " + _valorMaximo.toString() + " Anos"; 
		if (_valorMaximo == Integer.MAX_VALUE) return "Acima de " + _valorMinimo.toString() + " Anos"; 
		return "De " + _valorMinimo.toString() + " a " + _valorMaximo.toString() + " Anos";
	}
}