package com.bots.RacoonServer;

public abstract class Config {
    public static final char[] commandPrefixes = new char[]{'!', '$'};
    public static final boolean deleteMessagesContainingOnlyValidCommandCall = true;
    public static final String upVoteEmoji = "⬆️", downVoteEmoji = "⬇️";
    public static final int defaultPort = 3435;
    public static final int clientSocketSOTimeoutMS = 20;
    public static final int connectionExpireTimeForUnauthorizedConnectionsSeconds = 10;
}
