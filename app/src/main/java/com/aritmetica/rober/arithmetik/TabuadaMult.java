package com.aritmetica.rober.arithmetik;

public class TabuadaMult extends Tabuada{
	private final int[] resultados=new int[16];

	public TabuadaMult() {
		super("x");
		this.geraDesafio();
	}

	private void geraFatores() {
		operando1 = r.nextInt(this.maxTabuada) + 1;
		operando2 = r.nextInt(10) + 1;
	}

	private void geraResultadosPossiveis() {
		int i;
		int k = 8;
		for (i = 0; i <= 15; i++) {
			resultados[i]=this.operando1 * k;
			k++;
			if (k == 11) {
				k = 1;
			}
		}
	}

	private void geraRespostas() {
		int j;
		for (j = 0; j <= 3; j++) {
			respostas[j]=this.resultados[this.operando2 + 2 + j-this.posicaoCerta];
		}
	}

	public void geraDesafio() {
		this.geraFatores();
		this.geraPosicaoCerta();
		this.geraResultadosPossiveis();
		this.geraRespostas();
	}

	@Override
	public String getResposta(int x) {
		return Integer.toString(this.respostas[x]);
	}

	@Override
	public String PtoString(){
		return this.operando1+" "+this.getSinal()+" "+this.operando2;

	}
	@Override
	public int getFatorPontos() {
		return 1;
	}

}
