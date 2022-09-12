package com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.IncomingDataHandlers.IncomingOperationHandler;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
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
