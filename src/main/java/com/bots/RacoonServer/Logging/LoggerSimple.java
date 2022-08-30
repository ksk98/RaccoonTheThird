package com.bots.RacoonServer.Logging;

import com.bots.RacoonShared.Logging.Loggers.LoggerBase;
import com.bots.RacoonShared.Logging.Log;
import org.springframework.stereotype.Component;

@Component
public class LoggerSimple extends LoggerBase {
    public LoggerSimple() {
        super(1);
    }

    @Override
    public void log(Log log) {
        System.out.println(log.toString());
    }

    @Override
    public void logError(String message) {
        super.logError(">ERROR< " + message);
    }

    @Override
    public void logSuccess(String message) {
        super.logSuccess(">SUCCESS< " + message);
    }

    @Override
    public void logInfo(String message) {
        super.logInfo(">INFO< " + message);
    }
}
