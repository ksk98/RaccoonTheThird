package com.bots.RacoonServer.Logging.Loggers;

import com.bots.RacoonServer.Logging.Log;
import org.springframework.stereotype.Component;

@Component
public class LoggerSimple extends LoggerBase {
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
        super.logSuccess(">SUCCESS<" + message);
    }

    @Override
    public void logInfo(String message) {
        super.logInfo(">INFO<" + message);
    }
}
