package com.bots.RaccoonServer.Services;

import com.bots.RaccoonServer.Config;
import com.bots.RaccoonServer.Utility.MessageTools;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ReactionAddingService {
    public void addNecessaryReactionsTo(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (MessageTools.containsOrWillContainEmbeddedMedia(message)) {
            message.addReaction(Emoji.fromFormatted(Config.upVoteEmoji)).queue();
            message.addReaction(Emoji.fromFormatted(Config.downVoteEmoji)).queue();
        }
    }
}
