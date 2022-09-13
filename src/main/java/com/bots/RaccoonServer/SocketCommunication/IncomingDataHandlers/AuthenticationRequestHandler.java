package com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONOperationHandler;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import org.json.JSONObject;

import java.util.function.BiPredicate;

public class AuthenticationRequestHandler extends JSONOperationHandler {
    private final TrafficManager trafficManager;
    private final BiPredicate<String, String> validateCredentials;

    public AuthenticationRequestHandler(TrafficManager trafficManager, BiPredicate<String, String> validateCredentials) {
        super(SocketOperationIdentifiers.CLIENT_LOGIN);
        this.trafficManager = trafficManager;
        this.validateCredentials = validateCredentials;
    }

    @Override
    public void consume(JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");

        if (validateCredentials.test(username, password)) {
            trafficManager.getConnection(data.getInt("connection_id")).setAuthenticated(true);

            SocketCommunicationOperationBuilder builder =
                    new SocketCommunicationOperationBuilder()
                            .setData(new JSONObject()
                                    .put("response_code", 204)
                                    .put("client_operation_id", data.getInt("client_operation_id")));

            trafficManager.queueOperation(
                    trafficManager.getConnection(data.getInt("connection_id")),
                    builder.build()
            );
        } else {
            SocketCommunicationOperationBuilder builder =
                    new SocketCommunicationOperationBuilder()
                            .setData(new JSONObject()
                                    .put("response_code", 401)
                                    .put("message", "Bad credentials.")
                                    .put("client_operation_id", data.getInt("client_operation_id"))
                            );

            trafficManager.queueOperation(
                    trafficManager.getConnection(data.getInt("connection_id")),
                    builder.build()
            );
        }
    }
}
