package com.bots.RaccoonServer.Services.DiscordServices;

import com.bots.RaccoonServer.Converters.MessageEventConverter;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageLoggingService {
    private final ILogger logger;
    private final IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper;

    public MessageLoggingService(ILogger logger, IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper) {
        this.logger = logger;
        this.trafficServiceWrapper = trafficServiceWrapper;
    }

    public void logMessageReceived(MessageReceivedEvent event) {
        try { trafficServiceWrapper.queueBroadcast(MessageEventConverter.toMessageLog(event)); }
        catch (IOException e) {
            logger.logError(getClass().getName(), "Could not remotely log message received event: " + e);
        }
    }
}
