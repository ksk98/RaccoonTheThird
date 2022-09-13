package com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONOperationHandler;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import org.json.JSONObject;

public class DisconnectRequestHandler extends JSONOperationHandler {
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
