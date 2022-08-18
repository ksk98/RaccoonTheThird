package com.bots.RacoonServer.Controllers;

import com.bots.RacoonServer.Services.UpvoteCounting.UpvoteCountingService;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UpvoteController {
    private final UpvoteCountingService upvoteCountingService;

    public UpvoteController(UpvoteCountingService upvoteCountingService) {
        this.upvoteCountingService = upvoteCountingService;
    }

    @SubscribeEvent
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        upvoteCountingService.updatePoints(event);
    }

    @SubscribeEvent
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        upvoteCountingService.updatePoints(event);
    }
}
