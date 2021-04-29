package br.joao.blackJack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class RenderizadorMesa {
    HashMap<String, BufferedImage> imgsCartas;
    HashMap<String, BufferedImage> imgsResultados;
    int larguraCarta, alturaCarta;

    final int gorduraAltura = 10;
    final int gorduraLargura = 10;
    final int espacoEntreCartas = 10;

    final int alturaBnt = 30;
    final int larguraBnt = 88;
    final int espacoEntreBnt = 10;

    public RenderizadorMesa(String caminhoCartasImagens, String outros) {
        // cria um hashSet com todos nomes das cartas para serem carregadas
        //iniciarndo com 64 mas so 53 entradas serao usadas, para evitar colisoes
        HashSet<String> nomesCartas = new HashSet<>(64);
        for (Cartas carta : Cartas.values())
            nomesCartas.add(carta.name());
        imgsCartas = carregarConjuntoImagens(nomesCartas, caminhoCartasImagens);

        // inicia as variaveis de tamanhos das cartas usando o tamanho da carta 'COSTAS'
        BufferedImage imagemBaseTamanho = imgsCartas.get("COSTAS");
        larguraCarta = imagemBaseTamanho.getWidth();
        alturaCarta = imagemBaseTamanho.getHeight();

        // carrega essa outra parada aq
        HashSet<String> nomeJogadas = new HashSet<>(8);
        nomeJogadas.add("vitoria");
        nomeJogadas.add("derrota");
        imgsResultados = carregarConjuntoImagens(nomeJogadas, outros);
    }

    /**
     * vai carregar todas as imagem requisitadas da pasta especificada e retornar
     * elas
     * 
     * @param imagensNecessarias um HashSet de strings contento o nome das imagens
     *                           requisitadas
     * @param caminhoPastaImgs   caminho para a pasta que sera aberto as imagens
     * @return um HashMap com os nomes da imagems e sua respectiva imagem
     */
    private HashMap<String, BufferedImage> carregarConjuntoImagens(HashSet<String> imagensNecessarias,
            String caminhoPastaImgs) {

        //eu poderia ate comentar isso, mas seria muito redundante, ta muito bem explicadinho

        File pastaImgs = new File(caminhoPastaImgs);

        if (!pastaImgs.isDirectory()) {
            throw new IllegalArgumentException("o caminho " + caminhoPastaImgs + "nao é uma pasta");
        }

        File[] arquivosImg = pastaImgs.listFiles(arquivo -> arquivo.isFile() && arquivo.getName().endsWith(".png"));

        
        HashMap<String, BufferedImage> imagens = new HashMap<>();
        for (File arquivoImagem : arquivosImg) {
            String nomeImagem = arquivoImagem.getName();
            nomeImagem = nomeImagem.substring(0, nomeImagem.indexOf("."));

            if (imagensNecessarias.contains(nomeImagem)) {
                try {
                    BufferedImage imagem = ImageIO.read(arquivoImagem);
                    imagens.put(nomeImagem, imagem);
                } catch (IOException e) {
                    throw new IllegalArgumentException("a imagem" + nomeImagem + "nao pode ser aberta");
                }
            }
        }

        if (imagens.size() < imagensNecessarias.size()) {
            throw new IllegalArgumentException("a pasta " + caminhoPastaImgs + " nao tem imagens o suficiente");
        } else if (imagens.size() != imagensNecessarias.size()) {
            throw new IllegalArgumentException("nao sei pq mas a pasta " + caminhoPastaImgs + " esta meio estranha");
        }

        return imagens;
    }

    public BufferedImage renderizarMesa(MesaBlackJack mesa) {
        //para a mesa nao ficar larga sempre, ela fica com o espaco para 7 cartas se ela tiver <= 7 cartas
        //e aumentar para o tamanho maximo de 11 cartas caso tenha mais
        int larguraMesa;
        if (mesa.baralhoJogador.getBaralho().length <= 7) {
            larguraMesa = larguraCarta * 7 + espacoEntreCartas * 7 - 1 + gorduraLargura;
        } else {
            larguraMesa = larguraCarta * 11 + espacoEntreCartas * 11 - 1 + gorduraLargura;
        }
        
        int alturaMesa = alturaCarta * 3 + gorduraAltura;

        //criar a BufferedImage da mesa, e inicia o Graphics2D dela
        BufferedImage imagemMesa = new BufferedImage(larguraMesa, alturaMesa, BufferedImage.TYPE_INT_RGB);
        Graphics2D graficoMesa = (Graphics2D) imagemMesa.getGraphics();

        //pinta o fundo de verde
        graficoMesa.setPaint(Color.GREEN);
        graficoMesa.fillRect(0, 0, larguraMesa, alturaMesa);

        //renderiza as cartas de cima, que sao do dealer
        Cartas[] cartasDealer = mesa.baralhoDealer.getBaralho();
        desenharCartas(cartasDealer, gorduraAltura, graficoMesa);

        //renderiza as cartas de baixo, que sao do jogador
        Cartas[] cartasJogador = mesa.baralhoJogador.getBaralho();
        int baralhoJogadorY = alturaMesa - alturaCarta - gorduraAltura;
        desenharCartas(cartasJogador, baralhoJogadorY, graficoMesa);

        //se o jogo tiver acabado, coloca na tela o resultado dele
        if (!mesa.getJogando()) {
            BufferedImage resultado;
            if (mesa.resultadoJogo == ResultadoDaMesa.VENCIDA) {
                resultado = imgsResultados.get("vitoria");
                //TODO adiciona o caso de o jogo empatar
                // } else if(mesa.resultadoJogo == ResultadoDaMesa.PERDIDA){
            } else {
                resultado = imgsResultados.get("derrota");
            }
            //renderiza a o resultado
            int x = larguraMesa / 2 - resultado.getWidth() / 2;
            int y = alturaMesa / 2 - resultado.getHeight() / 2;
            graficoMesa.drawImage(resultado, x, y, null);
        }

        return imagemMesa;

    }

    //desenha o baralho de alguem horizontalmente na mesa
    private void desenharCartas(Cartas[] cartas, int y, Graphics2D g) {
        for (int i = 0; i < cartas.length; i++) {
            String nomeImg = cartas[i].name();
            BufferedImage imgCarta = imgsCartas.get(nomeImg);

            int x = gorduraLargura + larguraCarta * i + espacoEntreCartas * i;
            g.drawImage(imgCarta, x, y, null);
        }
    }
}