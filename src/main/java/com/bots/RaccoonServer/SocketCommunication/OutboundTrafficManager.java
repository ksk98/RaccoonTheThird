package com.bots.RaccoonServer.SocketCommunication;

import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperation;

public interface OutboundTrafficManager {
    void queueOperation(SocketConnection connection, SocketCommunicationOperation operation);
    void queueBroadcast(SocketCommunicationOperation operation);
}
