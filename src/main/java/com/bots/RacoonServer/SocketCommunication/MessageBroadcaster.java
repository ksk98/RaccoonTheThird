package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonServer.Converters.MessageEventConverter;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RacoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RacoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.IOException;

public class MessageBroadcaster {
    private final OutboundTrafficManager trafficManager;

    public MessageBroadcaster(OutboundTrafficManager trafficManager) {
        this.trafficManager = trafficManager;
    }

    public void broadcast(MessageReceivedEvent messageEvent) throws IOException {
        trafficManager.queueBroadcast(
                new SocketCommunicationOperationBuilder()
                        .setData(
                                new JSONObject()
                                        .put("operation", SocketOperationIdentifiers.LOG_MESSAGE_TO_CLIENT)
                                        .put("body", SerializationUtil.toString(MessageEventConverter.toMessageLog(messageEvent))))
                        .build()
        );
    }
}
