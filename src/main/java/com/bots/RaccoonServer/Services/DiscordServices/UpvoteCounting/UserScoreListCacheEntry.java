package com.bots.RaccoonServer.Services.DiscordServices.UpvoteCounting;

import kotlin.Pair;

import java.time.OffsetDateTime;
import java.util.List;

public class UserScoreListCacheEntry {
    public final String userId;
    public final List<Pair<String, Integer>> serverScores;
    public final long creationTimeEpochTimestamp;
    private String serverScoresString = null;

    public UserScoreListCacheEntry(String userId, List<Pair<String, Integer>> serverScores) {
        this.userId = userId;
        this.serverScores = serverScores;
        this.creationTimeEpochTimestamp = OffsetDateTime.now().toEpochSecond();
    }

    public String getListTextRepresentation() {
        if (serverScoresString == null) {
            StringBuilder stringBuilder = new StringBuilder();

            for (Pair<String, Integer> score: serverScores) {
                stringBuilder
                        .append(score.getFirst())
                        .append(": ")
                        .append(score.getSecond())
                        .append("\n");
            }

            serverScoresString = stringBuilder.toString();
        }

        return serverScoresString;
    }
}
