package br.joao.interacaoUser;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.dv8tion.jda.api.events.GenericEvent;

public class Interacao <T extends GenericEvent>{
    //O evento do JDA que vai executar
    private Class<T> classeEvento;
    //A condicao para a acao ser executada
    private Predicate<T> condicao;
    //Acao que e execudade se a condicao retornar true
    private Consumer<T> acao;
    //Outras interacoes associadas a essa
    //Serve para o Waiter remover classes associadas a essa
    private Interacao<?>[] irmaos;

    public Interacao(Class<T> classeEvento, Predicate<T> condicao, Consumer<T> acao){
        this.condicao = condicao;
        this.acao = acao;
        this.classeEvento = classeEvento;
    }

    public void setIrmaos(Interacao<?>[] irmaos){
        this.irmaos = irmaos;
    }

    public Interacao<?>[] getIrmaos(){
        return irmaos;
    }

    public Class<T> getClasseEvento() {
        return classeEvento;
    }

    //testa a condicao da interacao
    public boolean testar(GenericEvent evento){
        //primeira precisa ver se o evento é do mesmo tipo na classe
        if(classeEvento.isInstance(evento)){
            return condicao.test(classeEvento.cast(evento));
        }
        return false;
    }

    //executa a acao da interacao
    public void executar(GenericEvent evento){
        //primeira precisa ver se o evento é do mesmo tipo na classe
        if(classeEvento.isInstance(evento)){
            acao.accept(classeEvento.cast(evento));
        }
    }
}