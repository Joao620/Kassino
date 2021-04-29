package br.joao.interacaoUser;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

//so serve para passar uma lista de interacoes e o tempo de expiracao delas do InteracoesBuilder para o InteracoesWaiterh
public class InteracoesTransporter {
    private final Interacao<?>[] listaInteracoes;
    private final Runnable acaoExpiracao;
    private final long tempoExpiracao;
    private final TimeUnit unidadeTempo;
    
    public InteracoesTransporter(Interacao<?>[] listaInteracoes, long tempoExpiracao, TimeUnit unidadeTempo, Runnable acaoExpiracao) {
        this.listaInteracoes = listaInteracoes;
        this.tempoExpiracao = tempoExpiracao;
        this.unidadeTempo = unidadeTempo;
        this.acaoExpiracao = acaoExpiracao;
    }

    public InteracoesTransporter(Collection<Interacao<?>> listaInteracoes, long tempoExpiracao, TimeUnit unidadeTempo, Runnable acaoExpiracao) {
        this.listaInteracoes = new Interacao<?>[listaInteracoes.size()];
        int pos = 0;
        for (Interacao<?> interacao : listaInteracoes) {
            this.listaInteracoes[pos] = interacao;
            pos++;
        }

        this.tempoExpiracao = tempoExpiracao;
        this.unidadeTempo = unidadeTempo;
        this.acaoExpiracao = acaoExpiracao;
    }

    public Interacao<?>[] getInteracoes(){
        return listaInteracoes;
    }

    public Interacao<?> getInteracoes(int pos){
        return listaInteracoes[pos];
    }

    public long getTempoDeExpiracao(){
        return tempoExpiracao;
    }

    public TimeUnit getUnidadeDeTempo(){
        return unidadeTempo;
    }

    public Runnable getAcaoExpiracao() {
        return acaoExpiracao;
    }

}
