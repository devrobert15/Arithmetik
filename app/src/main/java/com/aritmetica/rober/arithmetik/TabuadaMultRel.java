package com.aritmetica.rober.arithmetik;

/**
 * Created by rober on 30/07/2016.
 */
public class TabuadaMultRel extends Tabuada{

    public TabuadaMultRel() {
        super("m");
        this.geraDesafio();
    }

    private void geraOperandos() {
        operando1 = (r.nextInt(maxTabuada) + 1)*geraSinal();
        operando2 = (r.nextInt(10) + 1)*geraSinal();
    }

    private Integer geraSinal() {
        if (r.nextBoolean()){
            return -1;
        }else {
            return 1;
        }
    }

    private void geraRespostas(int posicaoCerta) {
        int i;
        int k=posicaoCerta;
        for (i = 0; i <= 3; i++) {
            switch(i)
            {
                case 0:
                    // resposta certa
                    respostas[k]=this.operando1*this.operando2;
                    break;
                case 1:
                    respostas[k]=-this.operando1*this.operando2;
                    break;
                case 2:
                    respostas[k]=this.operando1*this.operando2-this.operando2;
                    if (this.operando1==1){
                        respostas[k]=this.operando1*this.operando2-this.operando2+11;
                    }
                    break;
                case 3:
                    respostas[k]=-(this.operando1*this.operando2-this.operando2);
                    if (this.operando1==1){
                        respostas[k]=-(this.operando1*this.operando2-this.operando2+11);
                    }
            }
            k++;
            if (k>3){
                k=0;
            }
        }

        if (posicaoCerta==0 || posicaoCerta==2){
                int temp=respostas[1];
            respostas[1]=respostas[3];
            respostas[3]=temp;
        }

    }

    public void geraDesafio() {
        this.geraOperandos();
        geraPosicaoCerta();
        this.geraRespostas(this.posicaoCerta);
    }

    private String getOperando1() {
        return Integer.toString(this.operando1);
    }

    private String getOperando2() {
        if (this.operando2>0){
            return Integer.toString(this.operando2);
        }else {
            return "(" + Integer.toString(this.operando2)+ ")";
        }
    }

    @Override
    public String getResposta(int x) {
        return Integer.toString(this.respostas[x]);
        }

    @Override
    public String PtoString(){
        return this.getOperando1()+" x "+this.getOperando2();

    }

    @Override
    public int getFatorPontos() {
        return 1;
    }

}
