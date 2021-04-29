package br.joao.blackJack;

public class MesaBlackJack {
    //se o jogo ainda esta rolando
    boolean jogando = true;
    //o jogador so pode dobrar na primeira rodada
    public boolean possibilidadeDobrar = true;

    //e setado depois que o jogo acaba
    public ResultadoDaMesa resultadoJogo = null;

    public Baralho baralhoJogador = new Baralho();
    public Baralho baralhoDealer = new Baralho();


    /**
     * Inicia o baralho do deler, do jogador
     */
    public MesaBlackJack() {
        baralhoDealer.adicionarCarta(Cartas.COSTAS);
        baralhoDealer.adicionarCarta(Cartas.cartaAleatoria());

        baralhoJogador.adicionarCarta(Cartas.cartaAleatoria());
        baralhoJogador.adicionarCarta(Cartas.cartaAleatoria());

        if (baralhoJogador.pontuacao() == 21) {
            finalizarJogo();
        }

    }

    public MesaBlackJack(String mesaSerializada) {
        deserializar(mesaSerializada);
    }

    public void gerarRodada(Jogadas jogada) {
        //caso de um bug e tente gerar uma rodada depois que o jogo acabou
        if (jogando == false) {
            return;
        }

        if (jogada == Jogadas.BATER || jogada == Jogadas.DOBRAR) {
            gerarCartasJogador();
        }
        if (baralhoJogador.pontuacao() > 21){
            perderJogo();
        }

        if (jogada == Jogadas.PARAR || jogada == Jogadas.DOBRAR || baralhoJogador.pontuacao() == 21) {
            finalizarJogo();
        }

        //depois da primeira rodada desabilita opc de dobrar
        possibilidadeDobrar = false;
    }

    private void finalizarJogo() {
        //substitui a carta que estava virada para baixo do dealer
        baralhoDealer.adicionarCarta(Cartas.cartaAleatoria(), 0);
        //adiciona cartas no baralho do dealer ate ficar maior que 17
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
        //so adiciona um carta para o jogador e checa se passar de 21
        Cartas novaCarta = Cartas.cartaAleatoria();
        baralhoJogador.adicionarCarta(novaCarta);
        int pontuacaoJogador = baralhoJogador.pontuacao();

        if (pontuacaoJogador > 21) {
            perderJogo();
        }
    }

    public Boolean getJogando() {
        return jogando;
    }

    public ResultadoDaMesa getResultadoDaMesa(){
        if(!jogando){
            return resultadoJogo;
        }
        return null;
    }

    public void perderJogo() {
        jogando = false;
        resultadoJogo = ResultadoDaMesa.PERDIDA;
    }

    public void vencerJogo() {
        jogando = false;
        resultadoJogo = ResultadoDaMesa.VENCIDA;
    }

    public void empatarJogo() {
        jogando = false;
        resultadoJogo = ResultadoDaMesa.EMPATE;
    }

    public String serializar() {
        // a mesa é serializada em uma string da seguinte forma
        // 21,jogando(1|0),dobrar(1|0),baralhoJogador,baralhoDealer
        StringBuilder mesaSerializada = new StringBuilder(64);
        mesaSerializada.append("21,");

        mesaSerializada.append((jogando) ? "1," : "0,");
        mesaSerializada.append((possibilidadeDobrar) ? "1," : "0,");

        mesaSerializada.append(baralhoJogador.serializar() + ",");
        mesaSerializada.append(baralhoDealer.serializar());

        return mesaSerializada.toString();
    }

    public boolean deserializar(String mesaSerializada) {
        //separa todos os campos da mesaSerializada
        String[] dadosMesa = mesaSerializada.split(",");

        //checa se ela possui o id 21 no comeco
        if(!dadosMesa[0].equals("21")){
            return false;
        }

        //pega os valores das variaveis
        this.jogando = (dadosMesa[1].equals("1")) ? true : false;
        this.possibilidadeDobrar = (dadosMesa[2].equals("1")) ? true : false;

        //pega os estados dos baralhos
        baralhoJogador.deserializar(dadosMesa[3]);
        baralhoDealer.deserializar(dadosMesa[4]);

        return true;
    }
}