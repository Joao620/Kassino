package br.joao.comandosDD.Jogo21;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import br.joao.blackJack.Jogadas;
import br.joao.blackJack.MesaBlackJack;
import br.joao.interacaoUser.InteracaoBuilder;
import br.joao.interacaoUser.InteracaoWaiter;
import br.joao.interacaoUser.InteracoesTransporter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

class EventosDeJogadas{
    EventosDeJogadas(User jogador, MessageChannel canalUsado, Message mensagemAnterior,
            MesaBlackJack mesaDoJogo, InteracaoWaiter waiter){
        InteracaoBuilder receberReacao = new InteracaoBuilder();

        receberReacao.addInteracao(MessageReceivedEvent.class, 
            validarEventoMensagem(jogador, canalUsado), 
            executarEventoMensagem(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter)
        );
        receberReacao.addInteracao(GuildMessageReactionAddEvent.class, 
        validarEventoEmoji(jogador, canalUsado), 
        executarEventoEmoji(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter)
        );

        receberReacao.addTemporizador(10, TimeUnit.SECONDS);
        receberReacao.addTimeOutAction(() -> {
            System.out.println("deu mole acabo o tempo");
        });
    }

    static InteracoesTransporter gerar(User jogador, MessageChannel canalUsado, Message mensagemAnterior,
            MesaBlackJack mesaDoJogo, InteracaoWaiter waiter){
                
        InteracaoBuilder receberReacao = new InteracaoBuilder();

        receberReacao.addInteracao(MessageReceivedEvent.class, 
            validarEventoMensagem(jogador, canalUsado), 
            executarEventoMensagem(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter)
        );

        receberReacao.addInteracao(GuildMessageReactionAddEvent.class, 
            validarEventoEmoji(jogador, canalUsado), 
            executarEventoEmoji(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter)
        );

        receberReacao.addTemporizador(10, TimeUnit.SECONDS);
        receberReacao.addTimeOutAction(() -> {
            System.out.println("deu mole acabo o tempo");
        });

        return receberReacao.build();
    }

    private static Consumer<MessageReceivedEvent> executarEventoMensagem(User jogador, MessageChannel canalUsado, Message mensagemAnterior,
            MesaBlackJack mesaDoJogo, InteracaoWaiter waiter) {
        return (MessageReceivedEvent e) -> {
            String msgRecebida = e.getMessage().getContentStripped();
            msgRecebida = msgRecebida.toLowerCase();
            Jogadas jogadaRealizada;
            
            switch (msgRecebida) {
                case "bater":
                    jogadaRealizada = Jogadas.BATER;
                    break;
                
                case "dobrar":
                    jogadaRealizada = Jogadas.DOBRAR;
                    break;

                case "parar":
                    jogadaRealizada = Jogadas.PARAR;
                    break;

                default:
                    return;
            }

            mesaDoJogo.gerarRodada(jogadaRealizada);
            ByteArrayOutputStream imgMesa = funcoesMesa.codificarImagemMesa(mesaDoJogo, K21.extensao);

            CompletableFuture<Message> imgEnviada = funcoesMesa.mandarImagemMesaParaDiscord(imgMesa, mesaDoJogo, e.getChannel(), jogador);
            mensagemAnterior.delete().queue();

            if (mesaDoJogo.getJogando()) {
                imgEnviada.thenAccept(msgEnviada -> {
                    // WaiterRodadas eventoDoido = new WaiterRodadas(jogador, canalUsado, msgEnviada, mesaDoJogo, waiter);
                    // waiter.addInteracoes(eventoDoido.gerarParada());
                    waiter.addInteracoes(EventosDeJogadas.gerar(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter));
                });
            } else {
                // waiterFinalizacao(jogador.getId());
            }
        };
    }

    private static Predicate<MessageReceivedEvent> validarEventoMensagem(User jogador, MessageChannel canalUsado) {
        return (MessageReceivedEvent e) -> {
            String msgRecebida = e.getMessage().getContentStripped();
            boolean mensgemValida = contemListaCaseInsensitive(msgRecebida, "bater", "dobrar", "parar");

            return !e.getAuthor().isBot() && (e.getAuthor().getIdLong() == jogador.getIdLong())
                    && (e.getChannel().getIdLong() == canalUsado.getIdLong()) && mensgemValida;
        };
    }

    private static Consumer<GuildMessageReactionAddEvent> executarEventoEmoji(User jogador, MessageChannel canalUsado,
            Message mensagemAnterior, MesaBlackJack mesaDoJogo, InteracaoWaiter waiter) {
        return (GuildMessageReactionAddEvent e) -> {
            String emojiEscolhido = e.getReaction().getReactionEmote().getEmoji();
            Jogadas jogadaEmoji =  EmojisEJogadas.getJogada(emojiEscolhido);
            if (jogadaEmoji == null) {
                return;
            }

            mesaDoJogo.gerarRodada(jogadaEmoji);
            ByteArrayOutputStream imgMesa = funcoesMesa.codificarImagemMesa(mesaDoJogo, K21.extensao);

            CompletableFuture<Message> imgEnviada = funcoesMesa.mandarImagemMesaParaDiscord(imgMesa, mesaDoJogo, e.getChannel(), jogador);
            mensagemAnterior.delete().queue();

            if (mesaDoJogo.getJogando()) {
                imgEnviada.thenAccept(msgEnviada -> {
                    // criarWaitEvents(jogador, canalUsado, msgEnviada, mesaDoJogo);
                    // WaiterRodadas eventoDoido = new WaiterRodadas(jogador, canalUsado, msgEnviada, mesaDoJogo, waiter);
                    // waiter.addInteracoes(eventoDoido.gerarParada());
                    waiter.addInteracoes(EventosDeJogadas.gerar(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter));
                });
            } else {
                // waiterFinalizacao(jogador.getId());
            }
        };
    }

    private static Predicate<GuildMessageReactionAddEvent> validarEventoEmoji(User jogador, MessageChannel canalUsado) {
        return (GuildMessageReactionAddEvent e) -> {
            //TODO: acontede que esse validador vai passar mesmo se algum usuario colocar um emoji invalido
            //que nesse caso so vai se pego no executar
            //e isso é errado, isso precisa ser pego aqui, no validador
            return !e.getUser().isBot() && (e.getUser().getIdLong() == jogador.getIdLong())
                    && (e.getChannel().getIdLong() == canalUsado.getIdLong());
        };
    }

    private static boolean contemListaCaseInsensitive(String comparante, String... comparadores) {
        for (String string : comparadores) {
            if (comparante.equalsIgnoreCase(string))
                return true;
        }

        return false;
    }
    
}
