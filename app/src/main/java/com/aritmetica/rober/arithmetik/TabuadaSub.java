package com.aritmetica.rober.arithmetik;

public class TabuadaSub extends Tabuada {

	public TabuadaSub() {
		super("-");
		this.geraDesafio();
	}

	private void geraParcelas() {
		operando1 = (r.nextInt(maxTabuada) + 1) * geraSinal();
		operando2 = (r.nextInt(10) + 1) * geraSinal();
	}

	private Integer geraSinal() {
		if (r.nextBoolean()) {
			return -1;
		} else {
			return 1;
		}
	}

	private void geraRespostas(int posicaoCerta) {
		int i;
		int k = posicaoCerta;
		for (i = 0; i <= 3; i++) {
			switch (i) {
				case 0:
					// resposta certa
					respostas[k] = this.operando1 - this.operando2;
					break;
				case 1:
					respostas[k] = -this.operando1 - this.operando2;
					if (this.operando1 == -this.operando2) {
						respostas[k] = -this.operando1 - this.operando2 + 1;
					}
					break;
				case 2:
					respostas[k] = this.operando1 + this.operando2;
					break;
				case 3:
					respostas[k] = -this.operando1 + this.operando2;
					if (this.operando1 == this.operando2) {
						respostas[k] = -this.operando1 + this.operando2 - 1;
					}
					break;
			}
			k++;
			if (k > 3) {
				k = 0;
			}
		}
	}

	public void geraDesafio() {
		this.geraParcelas();
		this.geraPosicaoCerta();
		this.geraRespostas(this.posicaoCerta);
	}

	private String getOperando1() {
		if (this.operando1 > 0) {
			return Integer.toString(this.operando1);
		} else {
			return Integer.toString(this.operando1);
		}
	}

	private String getOperando2() {
		if (this.operando2 > 0) {
			return "(+" + Integer.toString(this.operando2) + ")";
		} else {
			return "(" + Integer.toString(this.operando2) + ")";
		}
	}

	@Override
	public String getResposta(int x) {
		return Integer.toString(this.respostas[x]);
	}

	public String PtoString() {
		return this.getOperando1() + " " + this.getSinal() + " " + this.getOperando2();
	}

	@Override
	public int getFatorPontos() {
		return 3;
	}

}



