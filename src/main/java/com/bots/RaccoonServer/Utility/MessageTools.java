package com.bots.RaccoonServer.Utility;

import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageTools {
    public static boolean containsOrWillContainEmbeddedMedia(Message message) {
        if (message.getEmbeds().size() > 0)
            return true;

        // Sometimes during this check media will not yet embed.
        // As a fallback we can check if the message contains a link to a
        // piece of media that will probably be embedded after a while.

        // TODO: certain links that don't end in an extension are also embedded in discord
        // TODO: ex. youtube, twitter, reddit

        Pattern embeddableMediaLinksPattern = Pattern
                .compile("(?<=[^\\S]|^)https?://(.+/)+.+(\\.(jpg|jpeg|JPG|JPEG|png|PNG|gif|gifv|webm|mp4|wav|mp3|ogg))(?=[^\\S]|$)");
        Matcher embeddableMediaLinksMatcher = embeddableMediaLinksPattern.matcher(message.getContentRaw());

        return embeddableMediaLinksMatcher.find();
    }
}
