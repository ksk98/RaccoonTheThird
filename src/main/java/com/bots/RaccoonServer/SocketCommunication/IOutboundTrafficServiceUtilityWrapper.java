package com.bots.RaccoonServer.SocketCommunication;

import com.bots.RaccoonShared.Discord.MessageLog;
import com.bots.RaccoonShared.Discord.ServerChannels;
import com.bots.RaccoonShared.Logging.Log;

import java.io.IOException;
import java.util.LinkedList;

public interface IOutboundTrafficServiceUtilityWrapper extends IOutboundTrafficService {
    void queueOperation(SocketConnection connection, LinkedList<ServerChannels> serverChannelList) throws IOException;
    void queueBroadcast(LinkedList<ServerChannels> serverChannelList) throws IOException;
    void queueBroadcast(MessageLog messageLog) throws IOException;
    void queueBroadcast(Log log) throws IOException;
}
