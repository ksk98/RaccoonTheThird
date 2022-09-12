package com.bots.RaccoonServer.Controllers;

import com.bots.RaccoonServer.Services.MessageLoggingService;
import com.bots.RaccoonServer.Services.ProcessingService;
import com.bots.RaccoonServer.Services.ReactionAddingService;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MessageAndInteractionResponseController {
    private final ProcessingService processingService;
    private final ReactionAddingService reactionAddingService;
    private final MessageLoggingService messageLoggingService;

    public MessageAndInteractionResponseController(ProcessingService processingService, ReactionAddingService reactionAddingService, MessageLoggingService messageLoggingService) {
        this.processingService = processingService;
        this.reactionAddingService = reactionAddingService;
        this.messageLoggingService = messageLoggingService;
    }

    @SubscribeEvent
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.PRIVATE))
            messageLoggingService.logMessageReceived(event);

        if (event.getAuthor().isBot())
            return;

        processingService.processMessage(event);
        reactionAddingService.addNecessaryReactionsTo(event);
    }

    @SubscribeEvent
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        processingService.processInteraction(event);
        // TODO: log slash command use
    }
}
