package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonShared.Discord.BotMessage;
import com.bots.RacoonShared.IncomingDataHandlers.IncomingOperationHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RacoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class MessageSendRequestHandler extends IncomingOperationHandler {
    private final JDA jda;
    private final Logger logger;

    public MessageSendRequestHandler(JDA jda, Logger logger) {
        super(SocketOperationIdentifiers.SEND_MESSAGE_AS_BOT);
        this.jda = jda;
        this.logger = logger;
    }

    @Override
    public void consume(JSONObject data) {
        BotMessage message;
        try {
            message = (BotMessage) SerializationUtil.fromString(data.getString("body"));
        } catch (IOException | ClassNotFoundException e) {
            logger.logError(getClass().getName(), e.toString());
            return;
        }
        Guild guild = jda.getGuildById(message.serverId);
        TextChannel channel = Objects.requireNonNull(guild).getTextChannelById(message.channelId);
        Objects.requireNonNull(channel).sendMessage(message.message).queue();
    }
}
