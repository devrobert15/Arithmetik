package com.aritmetica.rober.arithmetik;

import java.util.Random;

abstract class Tabuada {
	private final String SINAL;
	final Random r = new Random();
	int maxTabuada;
	int operando1;
	int operando2;
	int posicaoCerta;
	final int[] respostas = new int[4];

	Tabuada(String sinal) {
		this.maxTabuada = 10;
		this.SINAL = sinal;
		Math.abs(-10);
	}

	public abstract void geraDesafio();

	public void setMaximo(int nivel) {
		this.maxTabuada = nivel;
	}

	public abstract String PtoString();

	void geraPosicaoCerta() {
		this.posicaoCerta = r.nextInt(4);
	}

	public abstract String getResposta(int x);

	public String getCerta() {
		return Integer.toString(this.posicaoCerta);
	}

	public String getSinal() {
		return this.SINAL;
	}

	public abstract int getFatorPontos();
}