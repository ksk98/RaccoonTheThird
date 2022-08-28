package com.bots.RacoonServer.Events.IncomingData;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Events.IncomingDataEvents.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.Events.IncomingDataEvents.IncomingDataTrafficHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiPredicate;

public class AuthenticationRequestHandler extends BaseIncomingDataTrafficHandler {
    private final TrafficManager trafficManager;
    private final BiPredicate<String, String> validateCredentials;

    public AuthenticationRequestHandler(IncomingDataTrafficHandler next, TrafficManager trafficManager, BiPredicate<String, String> validateCredentials) {
        super(next);
        this.trafficManager = trafficManager;
        this.validateCredentials = validateCredentials;
    }

    @Override
    public void handle(JSONObject response) {
        try {
            if (response.get("operation").equals("login")) {
                String username = response.getString("username");
                String password = response.getString("password");

                if (validateCredentials.test(username, password))
                    trafficManager.getConnection(response.getInt("connection_id")).setAuthenticated(true);

                return;
            }
        } catch (JSONException ignored) {}

        super.handle(response);
    }
}
