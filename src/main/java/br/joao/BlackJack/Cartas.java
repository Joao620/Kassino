package br.joao.BlackJack;

import java.util.Random;

public enum Cartas {
    PAUS2(2), PAUS3(3), PAUS4(4), PAUS5(5), PAUS6(6), PAUS7(7), PAUS8(8), PAUS9(9), PAUSJ(10), PAUSQ(10), PAUSK(10), PAUS11A(11),
    OUROS2(2), OUROS3(3), OUROS4(4), OUROS5(5), OUROS6(6), OUROS7(7), OUROS8(8), OUROS9(9), OUROSJ(10), OUROSQ(10), OUROSK(10), OUROS11A(11),
    COPAS2(2), COPAS3(3), COPAS4(4), COPAS5(5), COPAS6(6), COPAS7(7), COPAS8(8), COPAS9(9), COPASJ(10), COPASQ(10), COPASK(10), COPAS11A(11),
    ESPADAS2(2), ESPADAS3(3), ESPADAS4(4), ESPADAS5(5), ESPADAS6(6), ESPADAS7(7), ESPADAS8(8), ESPADAS9(9), ESPADASJ(10), ESPADASQ(10), ESPADASK(10), ESPADAS11A(11),
    //adicionados no final para nao serem incluidos na selecao aleatoria
    PAUS1A(1), OUROS1A(1), COPAS1A(1), ESPADAS1A(1);

    static Random random = new Random();

    private int valorCarta;
    Cartas(int valorCarta){
        this.valorCarta = valorCarta;
    }

    /**
     * Pick a random value of the BaseColor enum.
     * @return a random BaseColor.
     */
    public static Cartas cartaAleatoria() {
        //o -4 é para nao incluir os As com valor de 1
        int posicao = random.nextInt(values().length - 4);
        Cartas cartaAleatoria =  values()[posicao];


        return cartaAleatoria;
    }

    public static Cartas asCancelado(Cartas carta){
        switch (carta) {
            case PAUS11A:
                return PAUS1A;
            case OUROS11A:   
                return OUROS1A;

            case COPAS11A:   
                return COPAS1A;

            case ESPADAS11A:   
                return ESPADAS1A;
            
            default:
                return null;
        }
    }

    public int pontuacaoCarta() {
        return valorCarta;
    }
}
