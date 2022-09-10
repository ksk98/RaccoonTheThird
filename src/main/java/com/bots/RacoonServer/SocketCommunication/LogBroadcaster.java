package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonShared.Logging.Log;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RacoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RacoonShared.Util.SerializationUtil;
import org.json.JSONObject;

import java.io.IOException;

public class LogBroadcaster {
    private final OutboundTrafficManager trafficManager;

    public LogBroadcaster(OutboundTrafficManager trafficManager) {
        this.trafficManager = trafficManager;
    }

    public void broadcast(Log log) throws IOException {
        trafficManager.queueBroadcast(
                new SocketCommunicationOperationBuilder()
                        .setData(
                                new JSONObject()
                                        .put("operation", SocketOperationIdentifiers.LOG_SERVER_LOG_TO_CLIENT)
                                        .put("body", SerializationUtil.toString(log)))
                        .build()
                );
    }
}
