package br.joao.comandosDD.Jogo21;

import javax.annotation.Nullable;

class SetDeValorFixo <K, E> {
    private final Object[] primeiraLista;
    private final Object[] segundaLista;

    public SetDeValorFixo(K[] itensPrimeiraLista, E[] itensSegundaLista){
        if(itensPrimeiraLista.length != itensSegundaLista.length){
            new IllegalArgumentException("O tamanhos das listas são diferentes");
        }

        primeiraLista = (Object[]) itensPrimeiraLista;
        segundaLista = (Object[]) itensSegundaLista;     
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public E getSegundaLista(K jogadaRepresentativa){
        for (int i = 0; i < segundaLista.length; i++) {
            if(primeiraLista[i].equals(jogadaRepresentativa)){
                return (E) segundaLista[i];
            }
        }
        return null;
    }
    
    @Nullable
    @SuppressWarnings("unchecked")
    public K getPrimeiraLista(E stringRepresentativa){
        for (int i = 0; i < segundaLista.length; i++) {
            if(segundaLista[i].equals(stringRepresentativa)){
                return (K) primeiraLista[i];
            }
        }
        return null;
    }

    


}