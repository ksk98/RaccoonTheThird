package com.bots.RaccoonServer.Services.DiscordServices;

import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class JDAManagementService {
    private JDA jda;

    public JDAManagementService(ILogger logger, Environment environment) {
        try {
            jda = JDABuilder.createDefault(environment.getProperty("jda.token"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
        } catch (LoginException e) {
            logger.logError(
                    getClass().getName(),
                    e.toString()
            );
            System.exit(1);
        }
    }

    @Bean
    public JDA getJda() {
        return jda;
    }
}
