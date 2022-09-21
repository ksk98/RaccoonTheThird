package com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers;

import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonServer.Services.ClientServices.SocketConnection;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONOperationHandler;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import org.json.JSONObject;

import java.util.function.BiPredicate;

public class AuthenticationRequestHandler extends JSONOperationHandler {
    private final IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper;
    private final BiPredicate<String, String> validateCredentials;

    public AuthenticationRequestHandler(IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper, BiPredicate<String, String> validateCredentials) {
        super(SocketOperationIdentifiers.CLIENT_LOGIN);
        this.trafficServiceWrapper = trafficServiceWrapper;
        this.validateCredentials = validateCredentials;
    }

    @Override
    public void consume(JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");
        SocketConnection connection = trafficServiceWrapper.getConnectionForId(data.getInt("connection_id"));

        if (validateCredentials.test(username, password)) {
            connection.setAuthenticated(true);

            SocketCommunicationOperationBuilder builder =
                    new SocketCommunicationOperationBuilder()
                            .setData(new JSONObject()
                                    .put("response_code", 204)
                                    .put("client_operation_id", data.getInt("client_operation_id")));

            trafficServiceWrapper.queueOperation(connection, builder.build());
        } else {
            SocketCommunicationOperationBuilder builder =
                    new SocketCommunicationOperationBuilder()
                            .setData(new JSONObject()
                                    .put("response_code", 401)
                                    .put("message", "Bad credentials.")
                                    .put("client_operation_id", data.getInt("client_operation_id"))
                            );

            trafficServiceWrapper.queueOperation(connection, builder.build());
        }
    }
}
