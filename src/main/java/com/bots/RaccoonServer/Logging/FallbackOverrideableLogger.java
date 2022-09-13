package com.bots.RaccoonServer.Logging;

import com.bots.RaccoonShared.Logging.Log;
import com.bots.RaccoonShared.Logging.Loggers.Logger;

import java.util.function.BiConsumer;

public abstract class FallbackOverrideableLogger extends Logger {
    private final BiConsumer<Log, String> fallbackConsumer;

    protected FallbackOverrideableLogger(BiConsumer<Log, String> fallbackConsumer) {
        this.fallbackConsumer = fallbackConsumer;
    }

    @Override
    protected void fallbackLog(Log log, String error) {
        fallbackConsumer.accept(log, error);
    }
}
