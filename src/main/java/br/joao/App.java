package br.joao;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import br.joao.comandosDD.Jogo21.K21;
import br.joao.interacaoUser.InteracaoWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class App {
    static InteracaoWaiter waiter;

    public static void main(String[] args) {
        // String token = System.getenv("TOKEN_DISCORD");
        String token = System.getenv("TOKEN_BOT");
        String idDono = System.getenv("ID_DONO");

        // String localRedis = System.getenv("REDIS");
        // if(localRedis == null){
        //     localRedis = "localhost";
        // }

        // RedisDB.iniciarConexao(localRedis);


        CommandClient client = configurarBot(idDono);
        iniciarBot(token, client);

    }

    private static CommandClient configurarBot(String idDono) {
        CommandClientBuilder builder = new CommandClientBuilder();
        
        builder.setOwnerId(idDono);

        //Prefixos do bot
        builder.setPrefix("K");
        builder.setAlternativePrefix("!");
        
        waiter = new InteracaoWaiter();
        //Jedis conexaoRedis = RedisDB.retornaConexao();

        // Add commands
        builder.addCommand(new K21(waiter));

        return builder.build();
    }

    private static void iniciarBot(String token, CommandClient principalComandos){
        try {
            JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS)
                .disableCache(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE)
                .addEventListeners(principalComandos, waiter)
            .build();
        } catch (LoginException e) {
            System.out.println("alguma coisa deu ruim na autenticacao");
            System.out.println(e.getMessage());
            e.printStackTrace();
            
        }
    }
}