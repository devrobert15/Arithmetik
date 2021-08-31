package com.aritmetica.rober.arithmetik;

/**
 * Created by rober on 04/04/2017.
 */

public class TabuadaDivisao extends Tabuada {
    private String pergunta;
    private int indiceEscolhido;
    private final String[] lista={"1 : 1","2 : 1","3 : 1","4 : 1","5 : 1","6 : 1","7 : 1","8 : 1","9 : 1","10 : 1",
                      "2 : 2","4 : 2","6 : 2","8 : 2","10 : 2","12 : 2","14 : 2","16 : 2","18 : 2","20 : 2",
                      "3 : 3","6 : 3","9 : 3","12 : 3","15 : 3","18 : 3","21 : 3","24 : 3","27 : 3","30 : 3",
                      "4 : 4","8 : 4","12 : 4","16 : 4","20 : 4","24 : 4","28 : 4","32 : 4","36 : 4","40 : 4",
                      "5 : 5","10 : 5","15 : 5","20 : 5","25 : 5","30 : 5","35 : 5","40 : 5","45 : 5","50 : 5",
                      "6 : 6","12 : 6","18 : 6","24 : 6","30 : 6","36 : 6","42 : 6","48 : 6","54 : 6","60 : 6",
                      "7 : 7","14 : 7","21 : 7","28 : 7","35 : 7","42 : 7","49 : 7","56 : 7","63 : 7","70 : 7",
                      "8 : 8","16 : 8","24 : 8","32 : 8","40 : 8","48 : 8","56 : 8","64 : 8","72 : 8","80 : 8",
                      "9 : 9","18 : 9","27 : 9","36 : 9","45 : 9","54 : 9","63 : 9","72 : 9","81 : 9","90 : 9",
                      "10 : 10","20 : 10","30 : 10","40 : 10","50 : 10","60 : 10","70 : 10","80 : 10","90 : 10","100 : 10"};

    public TabuadaDivisao() {
        super(":");
        this.geraDesafio();
    }

    public void setMaximo(int nivel) {
        this.maxTabuada = 10*nivel;
    }
    private void geraRespostas(int posicaoCerta) {
        int i;
        int respostaCerta=(this.indiceEscolhido % 10 + 1) * (this.indiceEscolhido / 10 + 1) / (this.indiceEscolhido / 10 + 1);
        int k=posicaoCerta;
        for (i = 0; i <= 3; i++) {
            switch (i) {
                case 0:
                    // resposta certa
                    respostas[k] = respostaCerta;
                    break;
                case 1:
                    respostas[k] =respostaCerta-1;
                    break;
                case 2:
                    respostas[k] = respostaCerta+1 ;
                    break;
                case 3:
                    respostas[k] = respostaCerta+2;
            }
            k++;
            if (k > 3) {
                k = 0;
            }
        }
    }


    @Override
    public void geraDesafio() {
        // o valor máximo de maxTabuada é 100, logo o índice escolhido vai até 99
        this.indiceEscolhido=r.nextInt(maxTabuada);
        this.pergunta = lista[indiceEscolhido];
        geraPosicaoCerta();
        this.geraRespostas(this.posicaoCerta);
    }

    @Override
    public String PtoString() {
        return pergunta;
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
