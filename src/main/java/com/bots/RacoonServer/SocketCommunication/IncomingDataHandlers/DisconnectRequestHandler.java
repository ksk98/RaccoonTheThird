package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.IncomingDataHandlers.IncomingOperationHandler;
import com.bots.RacoonShared.SocketCommunication.SocketOperationIdentifiers;
import org.json.JSONObject;

public class DisconnectRequestHandler extends IncomingOperationHandler {
    private final TrafficManager trafficManager;

    public DisconnectRequestHandler(TrafficManager trafficManager) {
        super(SocketOperationIdentifiers.CLIENT_DISCONNECT);
        this.trafficManager = trafficManager;
    }

    @Override
    public void consume(JSONObject data) {
        trafficManager.removeConnection(data.getInt("connection_id"));
    }
}
