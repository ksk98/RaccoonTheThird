package com.bots.RacoonGeneral.Logging;

import com.bots.RacoonGeneral.Logging.Loggers.Logger;
import org.springframework.stereotype.Component;

@Component
public class CallerAcquirement {
    private final Logger logger;

    public CallerAcquirement(Logger logger) {
        this.logger = logger;
    }

    public String getMethodName() {
        return getMethodName(3);
    }

    public String getMethodName(int depth) {
        try {
            return Thread.currentThread().getStackTrace()[depth].getMethodName();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.logError(getClassName() + " received an empty stack trace array when trying to get a method name.");
        }

        return "???";
    }

    public String getClassName() {
        return getClassName(3);
    }

    public String getClassName(int depth) {
        try {
            return Thread.currentThread().getStackTrace()[depth].getClassName();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.logError("Utility class received an empty stack trace array when trying to get a class name.");
        }

        return "???";
    }
}
