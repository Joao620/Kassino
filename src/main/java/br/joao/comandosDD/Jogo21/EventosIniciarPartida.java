package br.joao.comandosDD.Jogo21;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import br.joao.blackJack.MesaBlackJack;
import br.joao.interacaoUser.InteracaoBuilder;
import br.joao.interacaoUser.InteracaoWaiter;
import br.joao.interacaoUser.InteracoesTransporter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

//TODO transforma isso numa subclasse de waiterTransporter
public class EventosIniciarPartida {
    public InteracoesTransporter gerar(User jogador, MessageChannel canalUsado, Message mensagemAnterior,
    MesaBlackJack mesaDoJogo, InteracaoWaiter waiter) {

        InteracaoBuilder receberReacao = new InteracaoBuilder();

        String[] emojisValidos = {"💵", "💶" ,"💷"};

        receberReacao.addInteracao(
            MessageReceivedEvent.class,
            new validarMensagem(jogador.getIdLong(), canalUsado.getIdLong()),
            new executarEventoMensagem(waiter, jogador, canalUsado, mensagemAnterior, mesaDoJogo)
        );

        receberReacao.addInteracao(
            GuildMessageReactionAddEvent.class,
            new validarEmoji(jogador.getIdLong(), canalUsado.getIdLong(), emojisValidos),
            new executarEventoEmoji(waiter, jogador, canalUsado, mensagemAnterior, mesaDoJogo)
        );

        receberReacao.addTemporizador(10, TimeUnit.SECONDS);
        receberReacao.addTimeOutAction(() -> {
            System.out.println("deu mole acabo o tempo");
        });

        return receberReacao.build();

    }

    private class validarMensagem implements Predicate<MessageReceivedEvent> {
        private long idJogador;
        private long idCanalUsado;

        validarMensagem(long idJogador, long idCanalUsado) {
            this.idJogador = idJogador;
            this.idCanalUsado = idCanalUsado;
        }

        @Override
        public boolean test(MessageReceivedEvent e) {
            String msgRecebida = e.getMessage().getContentStripped();
            boolean soNumeros = Pattern.matches("\\d+", msgRecebida);

            long idAutorMsg = e.getAuthor().getIdLong();
            long idCanalMsgRecebida = e.getChannel().getIdLong();

            return !e.getAuthor().isBot() // não eh um robo mandando mensagem
                    && (idAutorMsg == idJogador) // precisa ser o mesmo usuario que mandou a primeira mensagem
                    && (idCanalMsgRecebida == idCanalUsado) // no mesmo canal
                    && soNumeros; // e precisa ter ser so numeros na mensagem mandada
        }

    }

    private class executarEventoMensagem implements Consumer<MessageReceivedEvent> {
        private InteracaoWaiter waiter;
        private User jogador;
        private MessageChannel canalUsado;
        private Message mensagemAnterior;
        private MesaBlackJack mesaDoJogo;

        public executarEventoMensagem(InteracaoWaiter waiter, User jogador, MessageChannel canalUsado,
                Message mensagemAnterior, MesaBlackJack mesaDoJogo) {
            this.waiter = waiter;
            this.jogador = jogador;
            this.canalUsado = canalUsado;
            this.mensagemAnterior = mensagemAnterior;
            this.mesaDoJogo = mesaDoJogo;
        }

        @Override
        public void accept(MessageReceivedEvent t) {
            this.waiter.addInteracoes(EventosDeJogadas.gerar(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter));
        }
    }

    private class validarEmoji implements Predicate<GuildMessageReactionAddEvent> {
        private long idJogador;
        private long idCanalUsado;
        String emojisAceitados[];

        validarEmoji(long idJogador, long idCanalUsado, String emojisAceitados[]) {
            this.idJogador = idJogador;
            this.idCanalUsado = idCanalUsado;
            this.emojisAceitados = emojisAceitados;
            Arrays.sort(this.emojisAceitados);
        }

        @Override
        public boolean test(GuildMessageReactionAddEvent e) {
            long idAutorMsg = e.getUserIdLong();
            long idCanalMsgRecebida = e.getChannel().getIdLong();

            String emojiEnviado = e.getReaction().getReactionEmote().getEmoji();
            boolean emojiValido = Arrays.binarySearch(emojisAceitados, emojiEnviado) != -1 ? true : false;

            return !e.getUser().isBot() // não eh um robo mandando mensagem
                    && (idAutorMsg == idJogador) // precisa ser o mesmo usuario que mandou a primeira mensagem
                    && (idCanalMsgRecebida == idCanalUsado) // no mesmo canal
                    && emojiValido; //o emoji foi um dos pre definidos que serao aceitos
        }
    }

    private class executarEventoEmoji implements Consumer<GuildMessageReactionAddEvent>{
        private InteracaoWaiter waiter;
        private User jogador;
        private MessageChannel canalUsado;
        private Message mensagemAnterior;
        private MesaBlackJack mesaDoJogo;

        public executarEventoEmoji(InteracaoWaiter waiter, User jogador, MessageChannel canalUsado,
                Message mensagemAnterior, MesaBlackJack mesaDoJogo) {
            this.waiter = waiter;
            this.jogador = jogador;
            this.canalUsado = canalUsado;
            this.mensagemAnterior = mensagemAnterior;
            this.mesaDoJogo = mesaDoJogo;
        }

        @Override
        public void accept(GuildMessageReactionAddEvent t) {
            this.waiter.addInteracoes(EventosDeJogadas.gerar(jogador, canalUsado, mensagemAnterior, mesaDoJogo, waiter));
        }
    }

}
