package br.joao.comandosDD.Jogo21;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import br.joao.blackJack.MesaBlackJack;
import br.joao.interacaoUser.InteracaoWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import redis.clients.jedis.Jedis;

public class K21 extends Command {
    static final String extensao = "png";

    InteracaoWaiter waiter;
    Jedis clienteRedis;

    public K21(InteracaoWaiter IW) {
        this.name = "21";
        this.help = "inicia uma mesa de 21";
        this.arguments = "sla bro";
        this.guildOnly = false;
        this.waiter = IW;
        // this.clienteRedis = clienteRedis;
    }

    @Override
    protected void execute(CommandEvent event) {
        //TODO tem MUITO comentario aq, e pq eu fiquei cm preguica de consertar tudo isso antes de dar um git
        //entao a maioria das coisas vao funcionar no futuro de um jeito ou de 

        User jogador = event.getAuthor();
        MessageChannel canalUsado = event.getChannel();

        // boolean jaEstaJogando = clienteRedis.exists(jogador.getId());
        //boolean jaEstaJogando = false;

        // if(jaEstaJogando){
        // event.getChannel().sendMessage("opa, parece que voce ja ta num jogo
        // campeao").queue(msg ->{
        // msg.addReaction("😗").queue();
        // });
        // return;
        // }

        // EmbedBuilder eb = new EmbedBuilder();
        // eb.setTitle("Kassino 21");
        // eb.setColor(Color.green);
        // eb.setDescription("Jogo de 21 do bot kassino");
        // eb.addField("Como iniciar um jogo?", "K21 <valor para apostar>", true);
        // eb.addField("Ou selecione um emoji abaixo \ne já comece um jogo", " 💵 = 10R$ \n 💶 = 100R$ \n 💷 = 1000R$", true);
        // eb.addField("Ou mande agora um numero", "", false);
        // canalUsado.sendMessage(eb.build()).queue();

        MesaBlackJack mesa = new MesaBlackJack();
        ByteArrayOutputStream imgMesa = funcoesMesa.codificarImagemMesa(mesa, extensao);
        funcoesMesa.mandarImagemMesaParaDiscord(imgMesa, mesa, canalUsado, jogador).thenAccept(msgEnviada -> {
            waiter.addInteracoes(EventosDeJogadas.gerar(jogador, canalUsado, msgEnviada, mesa, waiter));
        });

        // clienteRedis.set(jogador.getId(), "21");
    }
}