package br.joao.comandosDD.Jogo21;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import br.joao.blackJack.Jogadas;
import br.joao.blackJack.MesaBlackJack;
import br.joao.blackJack.RenderizadorMesa;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

class funcoesMesa {
    static RenderizadorMesa rm = new RenderizadorMesa("Assets/cartas", "Assets/jogadas");

    public static CompletableFuture<Message> mandarImagemMesaParaDiscord(ByteArrayOutputStream imgMesa, MesaBlackJack mesa,
            MessageChannel canalEnvioImg, User usuario) {
        CompletableFuture<Message> msgEnviadaFuturo;
        if (mesa.getJogando()) {
            msgEnviadaFuturo = canalEnvioImg.sendFile(imgMesa.toByteArray(), "mesa.png").append(usuario.getAsMention())
                    .submit().thenApply(msg -> {
                        // thenApply serve para, logo que o future do submit for concluduido, que é
                        // quando quando a mensagem for enviada
                        // ele adiciona as reacoes nela
                        // Parece que se a pessoa ficar digitando k21 e bater mt rapido
                        // isso daqui vai tentar colocar um emoji numa mensagem que ja foi apagada
                        // entt talvez esse try conserte, mais sla
                        try {
                            msg.addReaction(EmojisEJogadas.getEmoji(Jogadas.BATER)).queue();

                            if (mesa.possibilidadeDobrar)
                            msg.addReaction(EmojisEJogadas.getEmoji(Jogadas.DOBRAR)).queue();

                            msg.addReaction(EmojisEJogadas.getEmoji(Jogadas.PARAR)).queue();
                        } catch (Exception e) {
                            System.out.println("parece que " + usuario.getName() + " esta digitando muito rapido");
                        }

                        return msg;

                    });

        } else {
            msgEnviadaFuturo = canalEnvioImg.sendFile(imgMesa.toByteArray(), "mesa.png")
                    .append("acabouse a partida campeao " + usuario.getAsMention()).submit();
        }

        return msgEnviadaFuturo;
    }

    //TODO muda essa nome, renderizar eh meio estranhos, alguma coisa como traduzir, translatar, codificar, etc
    public static ByteArrayOutputStream codificarImagemMesa(MesaBlackJack mesa, String extensao) {

        BufferedImage imgMesa = rm.renderizarMesa(mesa);
        ByteArrayOutputStream imgByte = new ByteArrayOutputStream();

        try {
            //aproveita e tira esse png hardcoded
            ImageIO.write(imgMesa, extensao, imgByte);
            imgByte.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return imgByte;
    }

}
