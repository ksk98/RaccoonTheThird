package com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RaccoonShared.Discord.BotMessage;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONOperationHandler;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import com.bots.RaccoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class MessageSendRequestHandler extends JSONOperationHandler {
    private final JDA jda;
    private final ILogger logger;

    public MessageSendRequestHandler(JDA jda, ILogger logger) {
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
        Guild guild = jda.getGuildById(message.serverId());
        TextChannel channel;

        try { channel = Objects.requireNonNull(guild).getTextChannelById(message.channelId()); }
        catch (NullPointerException e) {
            // Client tried to send message as bot in a channel bot is no longer in
            logger.logError(getClass().getName(), "Client tried to send message as bot in a channel bot is not in.");
            return;
        }

        Objects.requireNonNull(channel).sendMessage(message.message()).queue();
    }
}
