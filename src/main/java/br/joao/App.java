package br.joao;

import java.io.File;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class App extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        System.out.println("hello");
        String botToken = System.getenv("TOKEN_DISCORD");
        JDA jda = JDABuilder.createDefault(botToken, GatewayIntent.GUILD_MESSAGES)
                .disableCache(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.VOICE_STATE)
                .addEventListeners(new App()).build();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        File f;

        switch (event.getMessage().getContentRaw().charAt(0)) {
            case 'v':
                f = new File("video.mp4");
                break;
            case 'i':
                f = new File("cirilo.jpeg");
                break;
            case 'g':
                f = new File("gif.webp");
                break;
            default:
                return;
        }

        event.getChannel().sendFile(f, f.getName()).append("okay :)").queue();
    }
}