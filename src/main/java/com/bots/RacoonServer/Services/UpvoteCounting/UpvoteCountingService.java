package com.bots.RacoonServer.Services.UpvoteCounting;

import com.bots.RacoonServer.Config;
import com.bots.RacoonServer.Logging.Loggers.Logger;
import com.bots.RacoonServer.Persistence.UpvotePersistence.UserScore;
import com.bots.RacoonServer.Persistence.UpvotePersistence.UserScoreRepository;
import kotlin.Pair;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class UpvoteCountingService {
    private static final Map<String, Integer> reactionToPoints;
    static {
        reactionToPoints = new HashMap<>();
        reactionToPoints.put(Config.upVoteEmoji, 1);
        reactionToPoints.put(Config.downVoteEmoji, -1);
    }

    // Votes under a post are counted for a period of time
    // After that, further voting is ignored and the post is locked
    private final int postActivityInSeconds = 60 * 60 * 4;

    private final UserScoreRepository userScoreRepository;

    private final Map<String, UserScoreListCacheEntry> userScoreListCache;
    private final int cacheLifetimeInSeconds = 60 * 15;

    private final JDA jda;
    private final Logger logger;

    public UpvoteCountingService(UserScoreRepository userScoreRepository, @Lazy JDA jda, Logger logger) {
        this.userScoreRepository = userScoreRepository;
        this.jda = jda;
        this.logger = logger;
        userScoreListCache = new HashMap<>();
    }

    public int getPointsFor(String userId, String serverId) {
        return userScoreRepository.getUserScoresForUserAndServerId(userId, serverId).getPoints();
    }

    public String getPrintedListOfScoresForAllServers(String userId) {
        if (userScoreListCache.containsKey(userId) &&
                userScoreListCache.get(userId).creationTimeEpochTimestamp + cacheLifetimeInSeconds < OffsetDateTime.now().toEpochSecond()) {
            userScoreListCache.remove(userId);
        }

        if (!userScoreListCache.containsKey(userId)) {
            List<UserScore> scoresRaw = userScoreRepository.getUserScoresForUser(userId);
            List<Pair<String, Integer>> scoresProcessed = new ArrayList<>(scoresRaw.size());

            scoresRaw.forEach(score -> {
                try {
                    scoresProcessed.add(new Pair<>(
                            Objects.requireNonNull(jda.getGuildById(score.getServerId())).getName(),
                            score.getPoints())
                    );
                } catch (NullPointerException e) {
                    logger.logInfo("Could not obtain guild via JDA for id " + score.getServerId() + " while preparing score list for a user.");
                }
            });

            userScoreListCache.put(userId, new UserScoreListCacheEntry(userId, scoresProcessed));
        }

        return userScoreListCache.get(userId).getListTextRepresentation();
    }

    public void updatePoints(@NotNull GenericMessageReactionEvent event) {
        if (event.getChannelType().equals(ChannelType.PRIVATE))
            return;

        Message message = event.retrieveMessage().complete();
        if (message.getTimeCreated().toEpochSecond() + postActivityInSeconds >= OffsetDateTime.now().toEpochSecond())
            return;

        User user = event.retrieveUser().complete();
        if (user.isBot())
            return;

        if (event instanceof MessageReactionAddEvent)
            updatePoints(event, 1);
        // Theoretically this could've just been else because there are only 2 possibilities
        // for GenericMessageReactionEvent, but I'll leave it like that just in case
        else if (event instanceof MessageReactionRemoveEvent)
            updatePoints(event, -1);
    }

    private void updatePoints(GenericMessageReactionEvent event, int modifier) {
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        if (!reactionToPoints.containsKey(emoji))
            return;

        User user = event.retrieveMessage().complete().getAuthor();
        String serverId = event.getGuild().getId();
        if (!userScoreRepository.existsByUserAndServerId(user.getId(), serverId))
            userScoreRepository.save(new UserScore(user.getId(), serverId));

        userScoreRepository.alterPointsFor(
                user.getId(),
                serverId,
                reactionToPoints.get(emoji) * modifier
        );
    }
}
