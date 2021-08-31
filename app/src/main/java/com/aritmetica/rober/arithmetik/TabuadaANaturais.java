package com.aritmetica.rober.arithmetik;

public class TabuadaANaturais extends Tabuada {

    public TabuadaANaturais(){
        super("n");
        geraDesafio();
    }


    private void geraParcelas() {
        operando1 = (r.nextInt(maxTabuada) + 1);
        operando2 = (r.nextInt(10) + 1);
    }


    private void geraRespostas() {
        int i;
        int k=posicaoCerta;
        for (i = 0; i <= 3; i++) {
            switch(i)
            {
                case 0:
                    // resposta certa
                    respostas[k]=this.operando1+this.operando2;
                    break;
                case 1:
                    respostas[k]=this.operando1+this.operando2-1;
                    if (this.operando1==0 && this.operando2==0)
                        respostas[k]=this.operando1+this.operando2+3;
                    break;
                case 2:
                    respostas[k]=this.operando1+this.operando2+1;
                    break;
                case 3:
                    respostas[k]=this.operando1+this.operando2+2;
            }
            k++;
            if (k>3){
                k=0;
            }
        }
    }

    @Override
    public void geraDesafio() {
        this.geraParcelas();
        this.geraPosicaoCerta();
        this.geraRespostas();
    }

    @Override
    public String PtoString() {
        return Integer.toString(this.operando1)+" + "+Integer.toString(this.operando2);
    }

    @Override
    public String getResposta(int x) {
        return Integer.toString(this.respostas[x]);
    }

    @Override
    public int getFatorPontos() {
        return 1;
    }
}
