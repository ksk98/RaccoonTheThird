package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Controllers.MessageAndInteractionResponseController;
import com.bots.RaccoonServer.Controllers.ServerChannelAccessibilityChangeController;
import com.bots.RaccoonServer.Controllers.UpvoteController;
import com.bots.RaccoonServer.Services.DiscordServices.JDAManagementService;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventManagerConfig {
    private final AnnotatedEventManager eventManager;

    public EventManagerConfig(JDAManagementService JDAManagementService, MessageAndInteractionResponseController commandController,
                              UpvoteController upvoteController, ServerChannelAccessibilityChangeController accessibilityController) {
        this.eventManager = new AnnotatedEventManager();
        this.eventManager.register(commandController);
        this.eventManager.register(upvoteController);
        this.eventManager.register(accessibilityController);

        JDAManagementService.getJda().setEventManager(eventManager);
    }

    @Bean
    public AnnotatedEventManager getEventManager() {
        return eventManager;
    }
}
