package com.bots.RacoonRMI;

public interface LogConsumer {
    void log(String message, String caller);
    void logError(String message, String caller);
    void logSuccess(String message, String caller);
    void logInfo(String message, String caller);
}
