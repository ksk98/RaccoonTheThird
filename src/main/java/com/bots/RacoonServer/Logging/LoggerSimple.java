package com.bots.RacoonServer.Logging;

import com.bots.RacoonShared.Logging.Loggers.LoggerBase;
import com.bots.RacoonShared.Logging.Log;


public class LoggerSimple extends LoggerBase {
    public LoggerSimple() {
        super();
    }

    @Override
    public void log(Log log) {
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
