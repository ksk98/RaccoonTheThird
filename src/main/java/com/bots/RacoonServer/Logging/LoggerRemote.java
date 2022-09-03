package com.bots.RacoonServer.Logging;

import com.bots.RacoonServer.SocketCommunication.LogBroadcaster;
import com.bots.RacoonShared.Logging.Exceptions.LogException;
import com.bots.RacoonShared.Logging.Log;

import java.io.IOException;
import java.util.function.BiConsumer;

public class LoggerRemote extends FallbackOverrideableLogger {
    private final LogBroadcaster logBroadcaster;

    public LoggerRemote(BiConsumer<Log, String> fallbackConsumer, LogBroadcaster logBroadcaster) {
        super(fallbackConsumer);
        this.logBroadcaster = logBroadcaster;
    }

    @Override
    protected void displayLog(Log log) throws LogException {
        try {
            logBroadcaster.broadcast(log);
        } catch (IOException e) {
            throw new LogException(e.toString());
        }
    }
}
