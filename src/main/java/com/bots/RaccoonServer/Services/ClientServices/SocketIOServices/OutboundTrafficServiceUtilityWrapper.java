package com.bots.RaccoonServer.Services.ClientServices.SocketIOServices;

import com.bots.RaccoonServer.Services.ClientServices.SocketConnection;
import com.bots.RaccoonShared.Discord.MessageLog;
import com.bots.RaccoonShared.Discord.ServerChannels;
import com.bots.RaccoonShared.Logging.Log;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperation;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RaccoonShared.Util.SerializationUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

public class OutboundTrafficServiceUtilityWrapper implements IOutboundTrafficServiceUtilityWrapper {
    private final IOutboundTrafficService outboundTrafficService;

    public OutboundTrafficServiceUtilityWrapper(IOutboundTrafficService outboundTrafficService) {
        this.outboundTrafficService = outboundTrafficService;
    }

    private void sendOperationAndBodyTo(SocketConnection connection, SocketOperationIdentifiers operation, String body) {
        JSONObject content = new JSONObject()
                .put("operation", operation)
                .put("body", body);

        queueOperation(connection, new SocketCommunicationOperationBuilder().setData(content).build());
    }

    private void broadcastOperationAndBody(SocketOperationIdentifiers operation, String body) {
        JSONObject content = new JSONObject()
                .put("operation", operation)
                .put("body", body);

        queueBroadcast(new SocketCommunicationOperationBuilder().setData(content).build());
    }

    @Override
    public SocketConnection getConnectionForId(int connectionId) {
        return outboundTrafficService.getConnectionForId(connectionId);
    }

    @Override
    public void queueOperation(SocketConnection connection, SocketCommunicationOperation operation) {
        outboundTrafficService.queueOperation(connection, operation);
    }

    @Override
    public void queueBroadcast(SocketCommunicationOperation operation) {
        outboundTrafficService.queueBroadcast(operation);
    }

    @Override
    public void queueOperation(SocketConnection connection, LinkedList<ServerChannels> serverChannelList) throws IOException {
        sendOperationAndBodyTo(connection, SocketOperationIdentifiers.UPDATE_SERVER_CHANNEL_LIST, SerializationUtil.toString(serverChannelList));
    }

    @Override
    public void queueBroadcast(LinkedList<ServerChannels> serverChannelList) throws IOException {
        broadcastOperationAndBody(SocketOperationIdentifiers.UPDATE_SERVER_CHANNEL_LIST, SerializationUtil.toString(serverChannelList));
    }

    @Override
    public void queueBroadcast(MessageLog messageLog) throws IOException {
        broadcastOperationAndBody(SocketOperationIdentifiers.LOG_MESSAGE_TO_CLIENT, SerializationUtil.toString(messageLog));
    }

    @Override
    public void queueBroadcast(Log log) throws IOException {
        broadcastOperationAndBody(SocketOperationIdentifiers.LOG_SERVER_LOG_TO_CLIENT, SerializationUtil.toString(log));
    }
}
