package com.bots.RacoonServer.Controllers;

import com.bots.RacoonServer.Services.ProcessingService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EventListenerController extends ListenerAdapter {
    private final ProcessingService processingService;

    public EventListenerController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        processingService.processMessage(event);
        // TODO: log message logs
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        processingService.processInteraction(event);
        // TODO: log slash command use
    }
}
