package br.joao.BlackJack;

public class MesaBlackJack {
    boolean jogando = true;
    public boolean revelarCartaDealer = false;
    boolean flag21 = false;

    public EstadoMesa resultadoJogo = null;

    public Baralho baralhoJogador = new Baralho();
    public Baralho baralhoDealer = new Baralho();

    public Jogadas[] jogadasDisponiveis = new Jogadas[3];
    int rodada = 1;

    /**
     * Inicia o baralho do deler, do jogador, e as jogadas disponives
     */
    public MesaBlackJack() {
        baralhoDealer.adicionarCarta(Cartas.cartaAleatoria());
        baralhoDealer.adicionarCarta(Cartas.PAUS11A);

        baralhoJogador.adicionarCarta(Cartas.cartaAleatoria());
        baralhoJogador.adicionarCarta(Cartas.cartaAleatoria());

        if (baralhoJogador.pontuacao() == 21) {
            finalizarJogo();
        }

        jogadasDisponiveis[0] = Jogadas.DOBRAR;
        jogadasDisponiveis[1] = Jogadas.BATER;
        jogadasDisponiveis[2] = Jogadas.PARAR;
    }

    public void GerarRodada(Jogadas jogada) {
        if (jogando == false) {
            return;
        }
        
        incrementarRodada();

        if (jogada == Jogadas.BATER || jogada == Jogadas.DOBRAR) {
            gerarCartasJogador();
        }
        if (baralhoJogador.pontuacao() > 21){
            perderJogo();
        }

        if (jogada == Jogadas.PARAR || jogada == Jogadas.DOBRAR) {
            finalizarJogo();
        }
    }

    private void finalizarJogo() {
        int pontuacaoDealer = baralhoDealer.pontuacao();
        while (pontuacaoDealer < 17) {
            baralhoDealer.adicionarCarta(Cartas.cartaAleatoria());
            pontuacaoDealer = baralhoDealer.pontuacao();
        }

        if (pontuacaoDealer > 21) {
            vencerJogo();
        }
        else {
            int pontuacaoJogador = baralhoJogador.pontuacao();
            if (pontuacaoDealer < pontuacaoJogador) {
                vencerJogo();
                
            } else if (pontuacaoDealer > pontuacaoJogador) {
                perderJogo();
    
            } else if (pontuacaoDealer == pontuacaoJogador) {
                empatarJogo();
            }
        }

    }

    private void gerarCartasJogador() {
        Cartas novaCarta = Cartas.cartaAleatoria();
        baralhoJogador.adicionarCarta(novaCarta);
        int pontuacaoJogador = baralhoJogador.pontuacao();

        if (pontuacaoJogador > 21) {
            perderJogo();
        }
    }

    public Boolean getEstadoJogo() {
        return jogando;
    }

    public void perderJogo() {
        revelarCartaDealer = true;
        jogando = false;
        resultadoJogo = EstadoMesa.PERDIDA;
    }

    public void vencerJogo() {
        revelarCartaDealer = true;
        jogando = false;
        resultadoJogo = EstadoMesa.VENCIDA;
    }

    public void empatarJogo() {
        revelarCartaDealer = true;
        jogando = false;
        resultadoJogo = EstadoMesa.EMPATE;
    }

    public void incrementarRodada() {
        rodada += 1;
    }

}
