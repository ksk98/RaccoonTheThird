package com.bots.RaccoonServer.Services;

import com.bots.RaccoonShared.Discord.Channel;
import com.bots.RaccoonShared.Discord.ServerChannels;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildMessageChannel;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotIntelService {
    private final JDA jda;

    private LinkedList<ServerChannels> cachedList = null;
    private boolean cacheValid = false;

    public BotIntelService(JDA jda) {
        this.jda = jda;
    }

    public LinkedList<ServerChannels> getServerChannelList() {
        if (!cacheValid) {
            cachedList = jda.getGuilds().stream().map(guild -> {
                List<Channel> channels = guild.getTextChannels().stream()
                        .filter(GuildMessageChannel::canTalk)
                        .map(textChannel -> new Channel(textChannel.getId(), textChannel.getName()))
                        .toList();

                return new ServerChannels(guild.getId(), guild.getName(), channels);
            }).collect(Collectors.toCollection(LinkedList::new));

            cacheValid = true;
        }

        return cachedList;
    }

    public void invalidateCache() {
        cacheValid = false;
    }
}
