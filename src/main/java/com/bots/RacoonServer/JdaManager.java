package com.bots.RacoonServer;

import com.bots.RacoonServer.Controllers.EventListenerController;
import com.bots.RacoonServer.Logging.Loggers.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class JdaManager {
    private JDA jda;

    public boolean initialise(String token, ApplicationContext context) {
        Logger logger = context.getBean(Logger.class);

        try {
            jda = JDABuilder.createDefault(token)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(context.getBean(EventListenerController.class))
                    .build();
        } catch (LoginException e) {
            logger.logError(e.toString());
            return false;
        }

        return true;
    }
}
