package com.bots.RaccoonServer;

import com.bots.RaccoonServer.Converters.MessageEventConverter;
import com.bots.RaccoonShared.Discord.MessageLog;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import java.awt.*;
import java.util.LinkedList;

@ExtendWith(MockitoExtension.class)
public class ConverterTests {
    private final String guildId = "guildId", channelId = "channelId", messageContentRaw = "MCR";
    private final String authorUsername = "username", authorNickname = "nickname";
    private final Color memberColor = Color.GREEN;
    private final boolean userIsBot = false;

    @Test
    public void RegularScenario() {
        Guild guild = mockGuild();
        MessageChannelUnion channel = mockChannel();
        User user = mockUser();
        Member member = mockMember();
        Message message = mockMessage();

        MessageLog log = MessageEventConverter.toMessageLog(mockEvent(guild, channel, user, member, message));

        Assertions.assertEquals(guildId, log.serverId());
        Assertions.assertEquals(channelId, log.channelId());
        Assertions.assertEquals(messageContentRaw, log.message());
        Assertions.assertEquals(authorNickname, log.username());
        Assertions.assertEquals(memberColor, log.userColor());
        Assertions.assertEquals(userIsBot, log.userIsBot());
    }

    @Test
    public void MemberIsNull() {
        Guild guild = mockGuild();
        MessageChannelUnion channel = mockChannel();
        User user = mockUser();
        Message message = mockMessage();

        MessageLog log = MessageEventConverter.toMessageLog(mockEvent(guild, channel, user, null, message));

        Assertions.assertEquals(authorUsername, log.username());
        Assertions.assertEquals(MessageEventConverter.DEFAULT_MEMBER_COLOR, log.userColor());
    }

    private Guild mockGuild() {
        Guild out = mock(Guild.class);
        when(out.getId()).thenReturn(guildId);
        return out;
    }

    private MessageChannelUnion mockChannel() {
        MessageChannelUnion out = mock(MessageChannelUnion.class);
        when(out.getId()).thenReturn(channelId);
        return out;
    }

    private User mockUser() {
        User out = mock(User.class);

        lenient().when(out.getName()).thenReturn(authorUsername);
        when(out.isBot()).thenReturn(userIsBot);

        return out;
    }

    private Member mockMember() {
        Member out = mock(Member.class);

        lenient().when(out.getEffectiveName()).thenReturn(authorNickname);
        when(out.getColor()).thenReturn(memberColor);

        return out;
    }

    private Message mockMessage() {
        Message out = mock(Message.class);
        LinkedList<MessageEmbed> embeds = new LinkedList<>();
        MessageEmbed embed = mock(MessageEmbed.class);
        embeds.add(embed);

        when(out.getContentRaw()).thenReturn(messageContentRaw);
        when(out.getEmbeds()).thenReturn(embeds);

        return out;
    }

    private MessageReceivedEvent mockEvent(Guild guild, MessageChannelUnion channel, User user, Member member, Message message) {
        MessageReceivedEvent out = mock(MessageReceivedEvent.class);

        when(out.getGuild()).thenReturn(guild);
        when(out.getChannel()).thenReturn(channel);
        when(out.getAuthor()).thenReturn(user);
        when(out.getMember()).thenReturn(member);
        when(out.getMessage()).thenReturn(message);

        return out;
    }
}
