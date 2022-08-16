package com.bots.RacoonServer.Controllers;

import com.bots.RacoonServer.JdaManager;
import com.bots.RacoonServer.Services.ProcessingService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class EventListenerController {
    private final ProcessingService processingService;
    private final AnnotatedEventManager annotatedEventManager;

    public EventListenerController(ProcessingService processingService, JdaManager jdaManager) {
        this.processingService = processingService;
        this.annotatedEventManager = new AnnotatedEventManager();
        this.annotatedEventManager.register(this);
        jdaManager.getJda().setEventManager(annotatedEventManager);
    }

    @SubscribeEvent
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        processingService.processMessage(event);
        // TODO: log message logs
    }

    @SubscribeEvent
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        processingService.processInteraction(event);
        // TODO: log slash command use
    }
}
