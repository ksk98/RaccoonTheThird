package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.IncomingDataHandlers.BaseIncomingDataTrafficHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class DisconnectRequestHandler extends BaseIncomingDataTrafficHandler {
    private final TrafficManager trafficManager;

    public DisconnectRequestHandler(TrafficManager trafficManager) {
        this.trafficManager = trafficManager;
    }

    @Override
    public void handle(JSONObject data) {
        try {
            if (data.get("operation").equals("disconnect")) {
                trafficManager.removeConnection(data.getInt("connection_id"));
                return;
            }
        } catch (JSONException ignored) {}

        super.handle(data);
    }
}
