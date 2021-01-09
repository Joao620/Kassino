package br.joao.BlackJack;

public class Baralho {
    int quantCartas;
    Cartas[] cartasBaralho = new Cartas[11];
    int asNoBatalho = 0;

    public void adicionarCarta(Cartas carta) {
        try {
            cartasBaralho[quantCartas] = carta;
            if(carta.pontuacaoCarta() == 11){
                asNoBatalho += 1;
            }
            quantCartas += 1;
        } catch (Exception e) {
            return;
        }
    }

    public int pontuacao() {
        int soma = 0;
        for (int i = 0; i < quantCartas; i++) {
            soma += cartasBaralho[i].pontuacaoCarta();
        }

        if(soma > 21 && asNoBatalho > 0) {
            asNoBatalho -= 1;
            int indiceAs = 0;
            while(cartasBaralho[indiceAs].pontuacaoCarta() != 11){
                indiceAs += 1;
            }
            cartasBaralho[indiceAs] = Cartas.asCancelado(cartasBaralho[indiceAs]);
            soma -= 10;
        }

        return soma;
    }

    public Cartas[] getBaralho() {
        Cartas[] novaLista = new Cartas[quantCartas];
        for (int i = 0; i < novaLista.length; i++) {
            novaLista[i] = cartasBaralho[i];
        }
        return novaLista;
    }
}
