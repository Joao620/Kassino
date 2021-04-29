package br.joao.interacaoUser;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.Event;

//vai ser usada pra criar interacoes para ser adicionado no InteracaoUser
//ate agora eu so penso em adicionar reacao e mandar mensagem, tambem eu posso te varios desses
public class InteracaoBuilder {
    //lista das interacoes que vao sendo adicionadas com o tempo
    private HashSet<Interacao<?>> interacoes;
    //acao que vai ser executada quando o tempo acabar
    private Runnable acaoExpiracao;
    //tempo pra executar a acaoExpiracao
    private long tempoExpiracao;
    private TimeUnit unidadeTempo;

    public InteracaoBuilder() {
        interacoes = new HashSet<>();
    }

    
    public <T extends Event> void addInteracao(Class<T> classType, Predicate<T> condicao, Consumer<T> acao) {
        if(classType == null || condicao == null || acao == null){
            new IllegalArgumentException("paremetro nulo");
        }
        interacoes.add(new Interacao<>(classType, condicao, acao));
    }

    public void addTimeOutAction(Runnable timeoutActon){
        this.acaoExpiracao = timeoutActon;
    }

    public void addTemporizador(long tempoExpiracao, TimeUnit unidadeTempo){
        this.tempoExpiracao = tempoExpiracao;
        this.unidadeTempo = unidadeTempo;
    }

    //serve mais para iniciar a variavel irmao das classes de interacoes
    //e depois empacotar tudo isso em um InteracoesTransporter
    public InteracoesTransporter build(){
        if(acaoExpiracao == null || interacoes.isEmpty()){
            return null;
        }

        for (Interacao<?> interacaoRef : interacoes) {
            Interacao<?>[] irmaos = criarIrmaos(interacoes, interacaoRef);
            interacaoRef.setIrmaos(irmaos);
        }
        
        return new InteracoesTransporter(interacoes, tempoExpiracao, unidadeTempo, acaoExpiracao);
    }

    //cria uma copia da lista todasInteracoes, retirando siMesmo dela
    private Interacao<?>[] criarIrmaos(Collection<Interacao<?>> todasInteracoes, Interacao<?> siMesmo){
        Interacao<?>[] listaFinal = new Interacao<?>[todasInteracoes.size() - 1];

        int pos = 0;
        for (Interacao<?> interacao : todasInteracoes) {
            if(interacao != siMesmo){
                listaFinal[pos++] = interacao;
            }
        }

        return listaFinal;
    }
}

