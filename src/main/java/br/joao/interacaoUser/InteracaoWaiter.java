package br.joao.interacaoUser;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * Uma variavel do EventWaiter do Jda-Utilites
 * a diferenca é que ele pode ser varios EventsWaiters para uma coisa so
 * Como esperar por uma mesagem ou adicionar um emoji ao mesmo tempo
 * e a classe lida com tudo isso sozinha
 */
public class InteracaoWaiter implements EventListener {
    //TODO ta foda lek, toda hora esses hash maps dando erro
    //os eventos não são lançados assincronamente
    //vem um de cada vez bonitinho
    //o problema eh que voce colocou uns threads doidos aqui tlgd
    //e esses threads doidos devem estar fazendo uma baguncinha
    //usa um collection threadsafe ou loca essa poha
    private final Map<Class<?>, Set<Interacao<?>>> interacoesEmEspera;
    private final Map<Interacao<?>, ScheduledFuture<?>> acoesFuture;
    private final ScheduledThreadPoolExecutor scheduledExecutor;

    public InteracaoWaiter(){
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        interacoesEmEspera = new Hashtable<>();
        acoesFuture = new Hashtable<>();

    }

    //recebe um grupo de interacoes para ser esperadas em conjunto
    public void addInteracoes(InteracoesTransporter interacoesRecebidas){
        Interacao<?>[] ListaInteracoes = interacoesRecebidas.getInteracoes();
        if(ListaInteracoes == null || ListaInteracoes[0] == null){
            return;
        }

        ScheduledFuture<?> idSchedule = scheduledExecutor.schedule(() -> {
            removerInteracoesListaDeEspera(ListaInteracoes[0]);
            removerInteracoesAcoesFuture(ListaInteracoes[0]);
            interacoesRecebidas.getAcaoExpiracao().run();
        },
            interacoesRecebidas.getTempoDeExpiracao(), interacoesRecebidas.getUnidadeDeTempo());

        //vai rodar por todas interecoes e ir adicionando ela em interacoesEmEspera
        for (Interacao<?> interacaoDaVez : ListaInteracoes) {
            //nao o interacoesEmEspera nao estiver esperando pelo evento da interacaoDaVez ele cria essa chave
            Class<? extends GenericEvent> eventoInteracaoDaVez = interacaoDaVez.getClasseEvento();
            if(!interacoesEmEspera.containsKey(eventoInteracaoDaVez)){
                interacoesEmEspera.put(eventoInteracaoDaVez, new HashSet<>());
            }

            acoesFuture.put(interacaoDaVez, idSchedule);

            //adiciona a interacaoDaVez no Map na chave da classe de evento dela
            interacoesEmEspera.get(eventoInteracaoDaVez).add(interacaoDaVez);
        }

    }

    //aqui que a classe lidara com as acoes feitas pelo JDA
    @Override
    public void onEvent(GenericEvent event){
        //primeiro pego a classe do evento
        Class<?> c = event.getClass();
        //testa se a classe possui a chave para o evento
        if(!interacoesEmEspera.containsKey(c)){
            return;
        }
        //se possuir testa ainda se ela esta vazia
        if(interacoesEmEspera.get(c).isEmpty()){
            return;
        }
        
        //roda por todos as Interacoes na chave da classe do evento testando se alguma delas passa
        Set<Interacao<?>> ListaEventos = interacoesEmEspera.get(c);
        
        //TODO ainda ta dando problema de concurrent aqui aaaaaaaaaaaaa
        for (Interacao<?> interacao : ListaEventos) {
            if(interacao.testar(event)){
                //se alguma interacao passar, primeiro retiram a acao de expiracao dela
                acoesFuture.get(interacao).cancel(false);
                removerInteracoesAcoesFuture(interacao);
                //depois removem os irmaos dela
                removerInteracoesListaDeEspera(interacao);
                //e entao executam acao dela
                interacao.executar(event);
            }
        }
    }

    //remove todos os irmaos de uma interacao
    private void removerInteracoesListaDeEspera(Interacao<?> interacao){
        Interacao<?>[] irmaosParaRemover = interacao.getIrmaos();
        for (Interacao<?> interacaoIrmao : irmaosParaRemover) {
            Class<?> eventoIrmao = interacaoIrmao.getClasseEvento();
            //primeiro confirma se a classe possui o evento do irmao
            if(interacoesEmEspera.containsKey(eventoIrmao)){
                //se conter pega o set dentro da chave, e remove ele desse set
                interacoesEmEspera.get(eventoIrmao).remove(interacaoIrmao);
            }
        }
        
        //e entao remove ela mesma da classe
        interacoesEmEspera.get(interacao.getClasseEvento()).remove(interacao);
    }

    private void removerInteracoesAcoesFuture(Interacao<?> interacao){
        Interacao<?>[] irmaosParaRemover = interacao.getIrmaos();
        for (Interacao<?> interacaoIrmao : irmaosParaRemover) {
            acoesFuture.remove(interacaoIrmao);
        }

        acoesFuture.remove(interacao);
    }
}