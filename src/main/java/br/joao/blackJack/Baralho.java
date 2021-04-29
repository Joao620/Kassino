package br.joao.blackJack;

/**
 * conjunto de cartas usadas pelo dealor ou jogador
 */
public class Baralho {
    final int MAX_BARALHO = 11;

    int quantCartas;
    Cartas[] cartasBaralho = new Cartas[MAX_BARALHO];
    int asInteiros = 0;
    int asCancelados = 0;

    /**
     * adiciona uma carta no final do baralho
     * 
     * @param carta
     */
    public void adicionarCarta(Cartas carta) {
        // quantCartas usada pra adiciona a nova carta no final
        adicionarCarta(carta, quantCartas);
    }

    public void adicionarCarta(Cartas carta, int pos) {
        // se pos for < que quantCartas, entao esta fazendo uma substituicao
        // senao esta adicionando uma nova carta
        if (pos >= quantCartas) {
            quantCartas += 1;
        }

        // caso quando for adicionar mais cartas que o baralho aguente
        if (quantCartas > MAX_BARALHO) {
            return;
        }

        cartasBaralho[pos] = carta;
        //caso a carta seja um 21
        if (carta.pontuacaoCarta() == 11) {
            asInteiros += 1;
        }

    }

    public int pontuacao() {
        // faz a soma de todas as cartas no baralho
        int soma = 0;
        for (int i = 0; i < quantCartas; i++) {
            soma += cartasBaralho[i].pontuacaoCarta();
        }
        // se a pontuacao for maior q 21 e tiver um As
        if (soma > 21 && asInteiros > 0) {
            asInteiros -= 1;
            asCancelados += 1;
        }

        soma -= asCancelados * (11 - 1);

        return soma;
    }

    public Cartas[] getBaralho() {
        Cartas[] novaLista = new Cartas[quantCartas];
        for (int i = 0; i < novaLista.length; i++) {
            novaLista[i] = cartasBaralho[i];
        }
        return novaLista;
    }

    public String serializar() {
        //o braralho é serializado da seguinte maneira
        //um digito sendo 'asInteiros'
        //um digito sendo 'asCancelados'
        //depois duas letras para a abreviacao de cada carta, ex: CAP2P8

        StringBuilder baralhoSerializado = new StringBuilder(32);

        //para ter certeza e usar 2 digitos
        baralhoSerializado.append(String.valueOf(asInteiros));
        baralhoSerializado.append(String.valueOf(asCancelados));

        //adiciona todas as abreviacoes das cartas
        for (Cartas carta : getBaralho()) {
            baralhoSerializado.append(carta.serializar());
        }

        return baralhoSerializado.toString();

    }
    public void deserializar(String baralhoSerializado) {
        //-2 para desconsiderar as variaveis 'asInteiros' e 'asCancelados'
        //e /2 porque cara carta tem 2 caracteres
        quantCartas = (baralhoSerializado.length() - 2) / 2;
        String[] abreviacoesCartas = new String[quantCartas];

        //pega as variaveis
        this.asInteiros = Integer.parseInt(baralhoSerializado.substring(0, 1));
        this.asCancelados = Integer.parseInt(baralhoSerializado.substring(1, 2));

        //extrai todas as abreviacoes de cartas para o 'abreviacoesCartas'
        int posAbreviacao = 0;
        for (int i = 2; i < baralhoSerializado.length(); i+=2) {
            abreviacoesCartas[posAbreviacao++] = baralhoSerializado.substring(i, i+2);
        }

        //agora pega todas as abreviacoes e transforma em cartas propriamentes
        int posBaralho = 0;
        for (String abreviacao : abreviacoesCartas) {
            try {
                Cartas carta = Cartas.deserializar(abreviacao);
                cartasBaralho[posBaralho++] = carta;
            } catch (IllegalArgumentException e) {
                //caso a carta não exista
                System.out.println(e.getMessage());
                e.printStackTrace();
                cartasBaralho[posBaralho++] = Cartas.COSTAS;
            }
        }

        //escreve null nos resto das posicoes de 'cartasBaralho' caso o novo conjunto de cartas seja menor que o anterior
        while(cartasBaralho[posBaralho] != null){
            cartasBaralho[posBaralho++] = null;
        }
    }
}

