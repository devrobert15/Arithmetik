package com.aritmetica.rober.arithmetik;

/**
 * Created by rober on 15/05/2016.
 */
public class TabuadaExpNum extends Tabuada {

    private int operando3;

    public TabuadaExpNum() {
        super("*");
        this.geraDesafio();
    }

    @Override
    public void geraDesafio() {
        this.geraParcelas();
        this.geraPosicaoCerta();
        this.geraRespostas(this.posicaoCerta);
    }

    private Integer geraSinal() {
        if (r.nextBoolean()){
            return -1;
        }else {
            return 1;
        }
    }

    private void geraParcelas() {
        operando1 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        operando2 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        operando3 = (r.nextInt(maxTabuada)+1)*geraSinal();
    }

    private void geraRespostas(int posicaoCerta) {
        int i;
        int k=posicaoCerta;
        for (i = 0; i <= 3; i++) {
            switch(i)
            {
                case 0:
                    // resposta certa
                    respostas[k]=this.operando1+this.operando2+this.operando3;
                    break;
                case 1:
                    respostas[k]=this.operando1+this.operando2+this.operando3-1;
                    break;
                case 2:
                    respostas[k]=this.operando1+this.operando2+this.operando3+1;
                    break;
                case 3:
                    respostas[k]=this.operando1+this.operando2+this.operando3-2;
                    break;
            }
            k++;
            if (k>3){
                k=0;
            }
        }
    }

    @Override
    public String PtoString() {

        String op2= String.valueOf(this.operando2);
        String op3= String.valueOf(this.operando3);

        if (this.operando2>=0){
            op2="+"+this.operando2;
        }
        if (this.operando3>=0){
            op3="+"+ Integer.toString(this.operando3);
        }
        return this.operando1+op2+op3;
    }

    @Override
    public String getResposta(int x) {
        return Integer.toString(this.respostas[x]);
    }

    @Override
    public int getFatorPontos() {
        return 3;
    }
}
