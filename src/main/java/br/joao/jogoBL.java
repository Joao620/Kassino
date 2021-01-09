package br.joao;

import java.util.Scanner;

import br.joao.BlackJack.*;

public class jogoBL {
    void mostrarMesa(MesaBlackJack mesa) {
        System.out.println("BARALHO DEALER");
        Cartas[] Dealer = mesa.baralhoDealer.getBaralho();

        if(mesa.revelarCartaDealer){
            System.out.print(converterCarta(Dealer[0]));
        } else {
            System.out.print("#");
        }

        for (int i = 1; i < Dealer.length; i++) {
            System.out.print(" | " + converterCarta(Dealer[i]));
        }

        System.out.println("\n---------------");
        System.out.println("BARALHO JOGADOR");
        Cartas[] Jogador = mesa.baralhoJogador.getBaralho();

        System.out.print(converterCarta(Jogador[0]));

        for (int i = 1; i < Jogador.length; i++) {
            System.out.print(" | " + converterCarta(Jogador[i]));
        }

        System.out.println("");
    }

    Jogadas receberJogada(Jogadas[] jogadasDisponiveis, Scanner sc){
        System.out.print("Você Quer ");
        for (int i = 0; i < jogadasDisponiveis.length; i++) {
            if(i == jogadasDisponiveis.length - 1){
                System.out.print("ou ");
            }

            switch (jogadasDisponiveis[i]) {
                case BATER:
                    System.out.print("{B}ater ");
                    break;

                case DOBRAR:
                    System.out.print("{D}obrar ");
                    break;

                case PARAR:
                    System.out.print("{P}arar ");
                    break;
            
                default:
                    break;
            }
        }

        while (true) {
            String input = sc.nextLine();
            
            switch (input.toUpperCase()) {
                case "B":
                    return Jogadas.BATER;
                case "D":
                    return Jogadas.DOBRAR;
                case "P":
                    return Jogadas.PARAR;
                default:
                    System.out.println("entrar errada, tente denovo");
                    break;
            }
        }
    }

    private String converterCarta(Cartas carta){
        return Integer.toString(carta.pontuacaoCarta());
    }
    
}
