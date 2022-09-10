package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperation;

public interface OutboundTrafficManager {
    void queueOperation(SocketConnection connection, SocketCommunicationOperation operation);
    void queueBroadcast(SocketCommunicationOperation operation);
}
