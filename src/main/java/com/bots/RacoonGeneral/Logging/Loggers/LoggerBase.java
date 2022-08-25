package com.bots.RacoonGeneral.Logging.Loggers;

import com.bots.RacoonGeneral.Logging.Log;

public abstract class LoggerBase implements Logger {
    @Override
    public void log(String message) {
        log(new Log(message, COLOR_DEFAULT));
    }

    @Override
    public void logError(String message) {
        log(new Log(message, COLOR_ERROR));
    }

    @Override
    public void logSuccess(String message) {
        log(new Log(message, COLOR_SUCCESS));
    }

    @Override
    public void logInfo(String message) {
        log(new Log(message, COLOR_INFO));
    }
}
