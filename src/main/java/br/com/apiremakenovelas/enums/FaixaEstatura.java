package br.com.apiremakenovelas.enums;

import java.text.DecimalFormat;

public enum FaixaEstatura
{
	Abaixo_de_110_Cm(1, 1, 110),
	De_111_a_120_Cm(2, 111, 120),
	De_121_a_130_Cm(3, 121, 130),
	De_131_a_140_Cm(4, 131, 140),
	De_141_a_150_Cm(5, 141, 150),
	De_151_a_160_Cm(6, 151, 160),
	De_161_a_170_Cm(7, 161, 170),
	De_171_a_180_Cm(8, 171, 180),
	De_181_a_190_Cm(9, 181, 190),
	Acima_de_191_Cm(10, 191, 200);

	private final int id;
	private final double estaturaMinima, estaturaMaxima;
	private static final DecimalFormat formato = new DecimalFormat("0.00");

	FaixaEstatura(int id, double estaturaMinima, double estaturaMaxima)
	{
		this.id = id;
		this.estaturaMinima = estaturaMinima;
		this.estaturaMaxima = estaturaMaxima;
	}

	public int getId() { return id; }

	public double getValorMinimo() { return estaturaMinima; }

	public double getValorMaximo() { return estaturaMaxima; }

	public String getDescricao()
	{
		Double _valorMinimo = (this.getValorMinimo() / 100);
		Double _valorMaximo = (this.getValorMaximo() / 100);
		if (_valorMinimo == 0.01) return "Abaixo de " + formato.format(_valorMaximo + 0.01) + " m";
		if (_valorMaximo == 2.00) return "Acima de " + formato.format(_valorMinimo - 0.01) + " m"; 
		return "De " + formato.format(_valorMinimo) + " m até " + formato.format(_valorMaximo) + " m";
	}
}