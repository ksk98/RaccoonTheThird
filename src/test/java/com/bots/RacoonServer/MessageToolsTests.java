package com.bots.RacoonServer;

import com.bots.RacoonServer.Utility.MessageTools;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MessageToolsTests {
    @Test
    public void containsOrWillContainEmbeddedMediaTests() {
        String validLink = "https://i.redd.it/somelink.jpg";
        MessageBuilder messageBuilder = new MessageBuilder();

        // From embed
        messageBuilder.setEmbeds(new MessageEmbed(
                validLink,
                "title", "description",
                EmbedType.IMAGE, OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                0, null, null, null,
                null, null, null, null
        ));

        assertTrue(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));

        // From link in message content
        messageBuilder.clear();
        messageBuilder.append(validLink);
        assertTrue(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));

        messageBuilder.clear();
        messageBuilder.append("Hey look at this link: ");
        assertFalse(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));

        messageBuilder.append(validLink);
        assertTrue(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));

        // Period touches the link, thus it will no longer embed.
        messageBuilder.append(". It's great!");
        assertFalse(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));

        messageBuilder.setContent("Hey look at this link: " + validLink + " It's great!");
        assertTrue(MessageTools.containsOrWillContainEmbeddedMedia(messageBuilder.build()));
    }
}
