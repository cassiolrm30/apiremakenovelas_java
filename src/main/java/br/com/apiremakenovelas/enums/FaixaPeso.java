package br.com.apiremakenovelas.enums;

import java.text.DecimalFormat;

public enum FaixaPeso
{
	Abaixo_de_10_Kg(1, 1, 10),
	De_11_a_20_Kg(2, 11, 20),
	De_21_a_30_Kg(3, 21, 30),
	De_31_a_40_Kg(4, 31, 40),
	De_41_a_50_Kg(5, 41, 50),
	De_51_a_60_Kg(6, 51, 60),
	De_61_a_70_Kg(7, 61, 70),
	De_71_a_80_Kg(8, 71, 80),
	De_81_a_90_Kg(9, 81, 90),
	Acima_de_91_Kg(10, 91, 100);

	private final int id;
	private final double pesoMinimo, pesoMaximo;
	private static final DecimalFormat formato = new DecimalFormat("0");
	
	FaixaPeso(int id, double pesoMinimo, double pesoMaximo)
	{
		this.id = id;
		this.pesoMinimo = pesoMinimo;
		this.pesoMaximo = pesoMaximo;
	}

	public int getId() { return id; }

	public double getValorMinimo() { return pesoMinimo; }

	public double getValorMaximo() { return pesoMaximo; }

	public String getDescricao()
	{
		Double _valorMinimo = this.getValorMinimo();
		Double _valorMaximo = this.getValorMaximo();
		if (_valorMinimo == 1) return "Abaixo de " + formato.format(_valorMaximo + 1) + " kg";
		if (_valorMaximo == 100) return "Acima de " + formato.format(_valorMinimo - 1) + " kg"; 
		return "De " + formato.format(_valorMinimo) + " kg até " + formato.format(_valorMaximo) + " kg";
	}
}