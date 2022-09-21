package com.bots.RaccoonServer.Services.ClientServices.SocketIOServices;

import com.bots.RaccoonServer.Services.ClientServices.SocketConnection;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperation;

public interface IOutboundTrafficService {
    SocketConnection getConnectionForId(int connectionId);
    void queueOperation(SocketConnection connection, SocketCommunicationOperation operation);
    void queueBroadcast(SocketCommunicationOperation operation);
}
