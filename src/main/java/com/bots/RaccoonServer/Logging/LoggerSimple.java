package com.bots.RaccoonServer.Logging;

import com.bots.RaccoonShared.Logging.Log;

import java.util.function.BiConsumer;


public class LoggerSimple extends FallbackOverrideableLogger {

    protected LoggerSimple(BiConsumer<Log, String> fallbackConsumer) {
        super(fallbackConsumer);
    }

    @Override
    protected void displayLog(Log log) {
        System.out.println(log.toString());
    }

    @Override
    public void logError(String caller, String message) {
        super.logError(caller, ">ERROR< " + message);
    }

    @Override
    public void logSuccess(String caller, String message) {
        super.logSuccess(caller, ">SUCCESS< " + message);
    }

    @Override
    public void logInfo(String caller, String message) {
        super.logInfo(caller, ">INFO< " + message);
    }
}
