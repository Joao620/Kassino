package br.joao.comandosDD.Jogo21;

import br.joao.blackJack.Jogadas;

class EmojisEJogadas{
    //poderia so usar uns hashmaps, mas eu acho preguico e ineficiente em espaco
    //poderia criar uma implementacao para usar tipo um hashmap melhor, mas sem tempo irmao
    //entt vai com listas purar O(n) de search mesmo

    private static final SetDeValorFixo<Jogadas, String> b = new SetDeValorFixo<>(
        new Jogadas[]{Jogadas.BATER, Jogadas.DOBRAR, Jogadas.PARAR},
        new String[]{"👊", "✌", "🖐"}
    );

    private EmojisEJogadas(){}
    
    public static String getEmoji(Jogadas jogada){
        return b.getSegundaLista(jogada);
    }

    public static Jogadas getJogada(String emoji){
        return b.getPrimeiraLista(emoji);
    }
}
