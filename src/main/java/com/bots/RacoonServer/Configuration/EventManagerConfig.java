package com.bots.RacoonServer.Configuration;

import com.bots.RacoonServer.Controllers.CommandController;
import com.bots.RacoonServer.Controllers.UpvoteController;
import com.bots.RacoonServer.JdaManager;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventManagerConfig {
    private final AnnotatedEventManager eventManager;

    public EventManagerConfig(JdaManager jdaManager,
                              CommandController commandController, UpvoteController upvoteController) {
        this.eventManager = new AnnotatedEventManager();
        this.eventManager.register(commandController);
        this.eventManager.register(upvoteController);

        jdaManager.getJda().setEventManager(eventManager);
    }

    @Bean
    public AnnotatedEventManager getEventManager() {
        return eventManager;
    }
}
