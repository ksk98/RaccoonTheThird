package com.bots.RacoonServer.Services;

import com.bots.RacoonServer.Events.OnCreate.GenericOnCreateListener;
import com.bots.RacoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RacoonServer.SocketCommunication.MessageBroadcaster;
import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageLoggingService implements GenericOnCreateListener<TrafficManager> {
    private final Logger logger;
    private MessageBroadcaster messageBroadcaster = null;

    public MessageLoggingService(GenericOnCreatePublisher<TrafficManager> trafficManagerOnCreatePublisher, Logger logger) {
        this.logger = logger;
        trafficManagerOnCreatePublisher.subscribe(this);
    }

    public void logMessageReceived(MessageReceivedEvent event) {
        if (messageBroadcaster == null)
            return;

        try {
            messageBroadcaster.broadcast(event);
        } catch (IOException e) {
            logger.logError(getClass().getName(), "Could not remotely log message received event: " + e);
        }
    }

    @Override
    public void notify(TrafficManager created) {
        messageBroadcaster = new MessageBroadcaster(created);
    }
}
