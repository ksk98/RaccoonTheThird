package com.bots.RacoonServer.Events.IncomingData;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Events.IncomingDataEvents.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.Events.IncomingDataEvents.IncomingDataTrafficHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class DisconnectRequestHandler extends BaseIncomingDataTrafficHandler {
    private final TrafficManager trafficManager;

    public DisconnectRequestHandler(IncomingDataTrafficHandler next, TrafficManager trafficManager) {
        super(next);
        this.trafficManager = trafficManager;
    }

    @Override
    public void handle(JSONObject response) {
        try {
            if (response.get("operation").equals("disconnect")) {
                trafficManager.removeConnection(response.getInt("connection_id"));
                return;
            }
        } catch (JSONException ignored) {}

        super.handle(response);
    }
}
