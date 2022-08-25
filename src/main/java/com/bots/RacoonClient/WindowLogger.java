package com.bots.RacoonClient;

import com.bots.RacoonGeneral.Logging.Log;
import com.bots.RacoonGeneral.Logging.Loggers.LoggerBase;

// TODO:
public class WindowLogger extends LoggerBase {
    private static WindowLogger instance = null;

    private WindowLogger() {

    }

    public void initialise() {

    }

    public static WindowLogger getInstance() {
        return instance;
    }

    @Override
    public void log(Log log) {

    }
}
