package com.bots.RacoonServer.Logging;

import com.bots.RacoonShared.Logging.Log;
import com.bots.RacoonShared.Logging.Loggers.LoggerBase;

import java.util.function.BiConsumer;

public abstract class FallbackOverrideableLogger extends LoggerBase {
    private final BiConsumer<Log, String> fallbackConsumer;

    protected FallbackOverrideableLogger(BiConsumer<Log, String> fallbackConsumer) {
        this.fallbackConsumer = fallbackConsumer;
    }

    @Override
    protected void fallbackLog(Log log, String error) {
        fallbackConsumer.accept(log, error);
    }
}
