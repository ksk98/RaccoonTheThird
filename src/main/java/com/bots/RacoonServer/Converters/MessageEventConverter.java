package com.bots.RacoonServer.Converters;

import com.bots.RacoonShared.Discord.MessageLog;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Objects;

public class MessageEventConverter {
    public static MessageLog toMessageLog(MessageReceivedEvent event) {
        String username;
        try {
            username = Objects.requireNonNull(event.getMember()).getEffectiveName();
        } catch (NullPointerException ignored) {
            username = event.getAuthor().getName();
        }

        Color color = Color.BLACK;
        if (event.getMember() != null)
            color = Objects.requireNonNullElse(event.getMember().getColor(), Color.BLACK);

        MessageLog out = new MessageLog(
                event.getGuild().getId(),
                event.getChannel().getId(),
                username, color,
                event.getMessage().getContentRaw()
        );

        out.setUserIsBot(event.getAuthor().isBot());
        out.setHasEmbeds(!event.getMessage().getEmbeds().isEmpty());

        return out;
    }
}
