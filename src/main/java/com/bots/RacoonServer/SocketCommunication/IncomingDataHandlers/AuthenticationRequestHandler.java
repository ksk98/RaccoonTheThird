package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.IncomingDataHandlers.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.IncomingDataHandlers.IncomingDataTrafficHandler;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
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
    public void handle(JSONObject data) {
        try {
            if (data.getString("operation").equals("login")) {
                String username = data.getString("username");
                String password = data.getString("password");

                if (validateCredentials.test(username, password)) {
                    trafficManager.getConnection(data.getInt("connection_id")).setAuthenticated(true);

                    SocketCommunicationOperationBuilder builder =
                            new SocketCommunicationOperationBuilder()
                                    .setData(new JSONObject().append("response_code", 204));

                    trafficManager.queueOperation(
                            trafficManager.getConnection(data.getInt("connection_id")),
                            builder.build()
                    );
                } else {
                    SocketCommunicationOperationBuilder builder =
                            new SocketCommunicationOperationBuilder()
                                    .setData(new JSONObject()
                                            .append("response_code", 401)
                                            .append("message", "Bad credentials.")
                                    );

                    trafficManager.queueOperation(
                            trafficManager.getConnection(data.getInt("connection_id")),
                            builder.build()
                    );
                }

                return;
            }
        } catch (JSONException ignored) {}

        super.handle(data);
    }
}
