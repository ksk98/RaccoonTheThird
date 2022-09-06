package com.bots.RacoonServer.SocketCommunication.IncomingDataHandlers;

import com.bots.RacoonShared.IncomingDataHandlers.BaseIncomingDataTrafficHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.Util.SerializationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class MessageSendRequestHandler extends BaseIncomingDataTrafficHandler {
    private final JDA jda;
    private final Logger logger;

    public MessageSendRequestHandler(JDA jda, Logger logger) {
        this.jda = jda;
        this.logger = logger;
    }

    @Override
    public void handle(JSONObject data) {
        boolean operationIsSendMessage = false;

        try {
            operationIsSendMessage = data.getString("operation").equals("send_message");
        } catch (JSONException ignored) {}

        try {
            if (operationIsSendMessage) {
                Message message = (Message) SerializationUtil.fromString(data.getString("message"));
                Guild guild = jda.getGuildById(data.getString("guild_id"));
                TextChannel channel = Objects.requireNonNull(guild).getTextChannelById("channel_id");
                Objects.requireNonNull(channel).sendMessage(message).queue();

                return;
            }
        } catch (JSONException | ClassNotFoundException | IOException | NullPointerException e) {
            logger.logError(
                    getClass().getName(),
                    e.toString()
            );
        }

        super.handle(data);
    }
}
