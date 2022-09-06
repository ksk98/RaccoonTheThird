package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Discord.Channel;
import com.bots.RacoonShared.Discord.ServerChannels;
import com.bots.RacoonShared.IncomingDataHandlers.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import com.bots.RacoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ServerChannelRequestHandler extends BaseIncomingDataTrafficHandler {
    private final JDA jda;
    private final TrafficManager trafficManager;
    private final Logger logger;

    public ServerChannelRequestHandler(JDA jda, TrafficManager trafficManager, Logger logger) {
        this.jda = jda;
        this.trafficManager = trafficManager;
        this.logger = logger;
    }

    @Override
    public void handle(JSONObject data) {
        if (data.get("operation").equals("requestServerChannels")) {
            LinkedList<ServerChannels> out = new LinkedList<>();
            for (Guild guild: jda.getGuilds()) {
                out.add(new ServerChannels(
                        guild.getId(),
                        guild.getName(),
                        guild.getTextChannels().stream().map(
                                textChannel -> new Channel(textChannel.getId(), textChannel.getName()))
                                .collect(Collectors.toList())
                ));
            }

            try {
                SocketCommunicationOperationBuilder builder =
                        new SocketCommunicationOperationBuilder()
                                .setData(new JSONObject()
                                        .put("operation", "setServerChannelList")
                                        .put("body", SerializationUtil.toString(out)));
                trafficManager.queueOperation(
                        trafficManager.getConnection(data.getInt("connection_id")),
                        builder.build()
                );
            } catch (IOException e) {
                logger.logError(
                        getClass().getName(),
                        e.toString()
                );
            }
        } else super.handle(data);
    }
}
