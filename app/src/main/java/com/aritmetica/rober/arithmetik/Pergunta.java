package com.aritmetica.rober.arithmetik;

import java.io.Serializable;

class Pergunta implements Serializable {
	private final String txtper;
	private final String[] respostas=new String[4];
	private final String respCerta;
	private final String operacao;
	
	public Pergunta(String txtp, String rp1, String rp2, String rp3, String rp4, String rpCerta, String op){
		this.txtper=txtp;
		this.respostas[0]=rp1;
		this.respostas[1]=rp2;
		this.respostas[2]=rp3;
		this.respostas[3]=rp4;
		this.respCerta=rpCerta;
		this.operacao=op;
	}


	public String getTxtper(){
		return this.txtper;
	}

	public String getRespostas(int x){
		return this.respostas[x];
	}

	public String getRespCerta(){
		return this.respCerta;
	}
	public String getOperacao(){
		return this.operacao;
	}
	
	public String toString(){
		return this.txtper + "\t" + this.respostas[0] + "\t" + this.respostas[1] + "\t" + this.respostas[2] + "\t" + this.respostas[3] + "\t" + this.respCerta + "\t" + this.operacao + "\n";
	}
}
