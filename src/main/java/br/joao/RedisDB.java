package br.joao;

import redis.clients.jedis.Jedis;

public class RedisDB {
    static Jedis cliente;

    public static boolean iniciarConexao(String endereco) {    
        cliente = new Jedis(endereco);
        return true;
    }

    public static Jedis retornaConexao() {
        return cliente;
    }
}
