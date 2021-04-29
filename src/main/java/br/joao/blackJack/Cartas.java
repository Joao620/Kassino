package br.joao.blackJack;

import java.util.Random;

/**
 * lista todas as cartas disponiveis para serem usadas
 */
public enum Cartas {
    // PAUS2(2), PAUS3(3), PAUS4(4), PAUS5(5), PAUS6(6), PAUS7(7), PAUS8(8), PAUS9(9), PAUS10(10), PAUSJ(10), PAUSQ(10), PAUSK(10), PAUSA(11),
    // OUROS2(2), OUROS3(3), OUROS4(4), OUROS5(5), OUROS6(6), OUROS7(7), OUROS8(8), OUROS9(9), OUROS10(10), OUROSJ(10), OUROSQ(10), OUROSK(10), OUROSA(11),
    // COPAS2(2), COPAS3(3), COPAS4(4), COPAS5(5), COPAS6(6), COPAS7(7), COPAS8(8), COPAS9(9), COPAS10(10), COPASJ(10), COPASQ(10), COPASK(10), COPASA(11),
    // ESPADAS2(2), ESPADAS3(3), ESPADAS4(4), ESPADAS5(5), ESPADAS6(6), ESPADAS7(7), ESPADAS8(8), ESPADAS9(9), ESPADAS10(10), ESPADASJ(10), ESPADASQ(10), ESPADASK(10), ESPADASA(11),
    // COSTAS(0);

    PAUS2(2, "P2"), PAUS3(3, "P3"), PAUS4(4, "P4"), PAUS5(5, "P5"), PAUS6(6, "P6"), PAUS7(7, "P7"), PAUS8(8, "P8"), PAUS9(9, "P9"), PAUS10(10, "P1"), PAUSJ(10, "PJ"), PAUSQ(10, "PQ"), PAUSK(10, "PK"), PAUSA(11, "PA"), 
    OUROS2(2, "O2"), OUROS3(3, "O3"), OUROS4(4, "O4"), OUROS5(5, "O5"), OUROS6(6, "O6"), OUROS7(7, "O7"), OUROS8(8, "O8"), OUROS9(9, "O9"), OUROS10(10, "O1"), OUROSJ(10, "OJ"), OUROSQ(10, "OQ"), OUROSK(10, "OK"), OUROSA(11, "OA"), 
    COPAS2(2, "C2"), COPAS3(3, "C3"), COPAS4(4, "C4"), COPAS5(5, "C5"), COPAS6(6, "C6"), COPAS7(7, "C7"), COPAS8(8, "C8"), COPAS9(9, "C9"), COPAS10(10, "C1"), COPASJ(10, "CJ"), COPASQ(10, "CQ"), COPASK(10, "CK"), COPASA(11, "CA"), 
    ESPADAS2(2, "E2"), ESPADAS3(3, "E3"), ESPADAS4(4, "E4"), ESPADAS5(5, "E5"), ESPADAS6(6, "E6"), ESPADAS7(7, "E7"), ESPADAS8(8, "E8"), ESPADAS9(9, "E9"), ESPADAS10(10, "E1"), ESPADASJ(10, "EJ"), ESPADASQ(10, "EQ"), ESPADASK(10, "EK"), ESPADASA(11, "EA"), 
    COSTAS(0, "CS");

    //objeto random statico para ser usados entre todas as classes
    static Random random = new Random();

    private int valorCarta;
    private String abreviacao;
    Cartas(int valorCarta, String abreviacao){
        this.valorCarta = valorCarta;
        this.abreviacao = abreviacao;
    }

    //retorna uma carta aleatoria
    public static Cartas cartaAleatoria() {
        int posicao = random.nextInt(values().length - 1); //-1 para excluir a cartas COSTA
        Cartas cartaAleatoria =  values()[posicao];
        return cartaAleatoria;
    }

    /**
     * retorna a pontuacao da carta
     */
    public int pontuacaoCarta() {
        return valorCarta;
    }

    public String serializar() {
        //so retorna sua abreviacao mesmo
        return this.abreviacao;
    }

    public static Cartas deserializar(String abreviacao) {
        //checa se a 'cartaAbreviada' é igual a alguma abreviacao na classe cartas
        for(Cartas carta : Cartas.values()){
            if(abreviacao.equals(carta.abreviacao)){
                return carta;
            }
        }

        throw new IllegalArgumentException("a abreviacao da carta " + abreviacao + " nao existe");
    }
}
