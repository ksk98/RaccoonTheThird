package com.bots.RacoonServer.Controllers;

import com.bots.RacoonServer.Services.ProcessingService;
import com.bots.RacoonServer.Services.ReactionAddingService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MessageAndInteractionResponseController {
    private final ProcessingService processingService;
    private final ReactionAddingService reactionAddingService;

    public MessageAndInteractionResponseController(ProcessingService processingService, ReactionAddingService reactionAddingService) {
        this.processingService = processingService;
        this.reactionAddingService = reactionAddingService;
    }

    @SubscribeEvent
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        processingService.processMessage(event);
        reactionAddingService.addNecessaryReactionsTo(event);
        // TODO: log message logs
    }

    @SubscribeEvent
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        processingService.processInteraction(event);
        // TODO: log slash command use
    }
}
