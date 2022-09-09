package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Discord.Channel;
import com.bots.RacoonShared.Discord.ServerChannels;
import com.bots.RacoonShared.IncomingDataHandlers.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.IncomingDataHandlers.IncomingOperationHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RacoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RacoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ServerChannelRequestHandler extends IncomingOperationHandler {
    private final JDA jda;
    private final TrafficManager trafficManager;
    private final Logger logger;

    public ServerChannelRequestHandler(JDA jda, TrafficManager trafficManager, Logger logger) {
        super(SocketOperationIdentifiers.REQUEST_SERVER_CHANNEL_LIST);
        this.jda = jda;
        this.trafficManager = trafficManager;
        this.logger = logger;
    }

    @Override
    public void consume(JSONObject data) {
        LinkedList<ServerChannels> serverChannelsList = jda.getGuilds().stream().map(guild -> {
            List<Channel> channels = guild.getTextChannels().stream().map(
                    textChannel -> new Channel(textChannel.getId(), textChannel.getName())
            ).toList();

            return new ServerChannels(guild.getId(), guild.getName(), channels);
        }).collect(Collectors.toCollection(LinkedList::new));

        JSONObject outData;
        try {
            outData = new JSONObject()
                    .put("operation", SocketOperationIdentifiers.UPDATE_SERVER_CHANNEL_LIST)
                    .put("body", SerializationUtil.toString(serverChannelsList));
        } catch (IOException e) {
            logger.logError(getClass().getName(), e.toString());
            return;
        }

        SocketCommunicationOperationBuilder builder = new SocketCommunicationOperationBuilder().setData(outData);
        trafficManager.queueOperation(trafficManager.getConnection(data.getInt("connection_id")), builder.build());
    }
}
