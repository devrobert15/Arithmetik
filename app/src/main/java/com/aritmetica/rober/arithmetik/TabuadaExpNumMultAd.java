package com.aritmetica.rober.arithmetik;


/**
 * Created by rober on 29/12/2016.
 */

public class TabuadaExpNumMultAd extends Tabuada {
    private static boolean mult;

    private int operando3;

    public TabuadaExpNumMultAd() {
        super("ma");
        this.geraDesafio();
    }

    @Override
    public void geraDesafio() {
        this.geraOperandos();
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

    private void Multiplicacao(){
        mult = geraSinal() == 1;
    }

    private void geraOperandos() {
        operando1 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        operando2 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        operando3 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        this.Multiplicacao();
    }


    private void geraRespostas(int posicaoCerta){
        int i;
        int k=posicaoCerta;

        if (mult) {
            for (i = 0; i <= 3; i++) {
                switch (i) {
                    case 0:
                        // resposta certa
                        respostas[k] = this.operando1 * this.operando2 + this.operando3;
                        break;
                    case 1:
                        respostas[k] = this.operando1 * this.operando2 + this.operando3-1;
                        break;
                    case 2:
                        respostas[k] = this.operando1 * this.operando2 + this.operando3+1;
                        break;
                    case 3:
                        respostas[k] = this.operando1 * this.operando2 + this.operando3 + 2;
                        break;
                }
                k++;
                if (k > 3) {
                    k = 0;
                }
            }
        }else {
            for (i = 0; i <= 3; i++) {
                switch (i) {
                    case 0:
                        // resposta certa
                        respostas[k] =this.operando1+this.operando2*this.operando3;
                        break;
                    case 1:
                        respostas[k] =this.operando1+this.operando2*this.operando3-1;
                        break;
                    case 2:
                        respostas[k] =this.operando1+this.operando2*this.operando3+1;
                        break;
                    case 3:
                        respostas[k] =this.operando1+this.operando2*this.operando3+2;
                        break;
                }
                k++;
                if (k > 3) {
                    k = 0;
                }
            }

        }

    }

    public String PtoString() {
        String op2= String.valueOf(this.operando2);
        String op3= String.valueOf(this.operando3);
        String response="";

        if (mult){
            if (operando2<0){
                op2="(" + Integer.toString(this.operando2) + ")";
            }
            if (operando3>=0){
                op3="+"+ Integer.toString(this.operando3);
            }
            response = this.operando1+"x"+ op2 + op3;
        } else {
            if (operando3<0){
                op3="(" + Integer.toString(this.operando3) + ")";
            }
            if (operando2>=0){
                op2="+"+ Integer.toString(this.operando2);
            }
            response = this.operando1+op2+"x"+op3;
        }
        return response;
    }

    @Override
    public String getResposta(int x) {
        return Integer.toString(this.respostas[x]);
    }

    @Override
    public int getFatorPontos() {
        return 4;
    }

}
