package com.bots.RacoonServer.Logging.Loggers;

import com.bots.RacoonServer.Logging.Log;

public abstract class LoggerBase implements Logger {
    @Override
    public void log(String message) {
        log(new Log(message, Logger.COLOR_DEFAULT));
    }

    @Override
    public void logError(String message) {
        log(new Log(message, Logger.COLOR_ERROR));
    }

    @Override
    public void logSuccess(String message) {
        log(new Log(message, Logger.COLOR_SUCCESS));
    }

    @Override
    public void logInfo(String message) {
        log(new Log(message, Logger.COLOR_INFO));
    }
}
