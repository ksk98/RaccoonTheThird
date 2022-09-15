package com.bots.RaccoonServer.Converters;

import com.bots.RaccoonShared.Discord.MessageLog;
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
                event.getMessage().getContentRaw(),
                event.getAuthor().isBot(),
                !event.getMessage().getEmbeds().isEmpty()
        );

        return out;
    }
}
