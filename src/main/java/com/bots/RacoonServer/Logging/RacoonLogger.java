package com.bots.RacoonServer.Logging;

import com.bots.RacoonServer.Events.OnCreate.GenericOnCreateListener;
import com.bots.RacoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RacoonServer.SocketCommunication.LogBroadcaster;
import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Logging.Log;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.Logging.Loggers.LoggerBase;

public class RacoonLogger extends LoggerBase implements GenericOnCreateListener<TrafficManager> {
    private final Logger localLogger;
    private Logger remoteLogger = null;

    public RacoonLogger(GenericOnCreatePublisher<TrafficManager> trafficManagerOnCreatePublisher) {
        trafficManagerOnCreatePublisher.subscribe(this);
        localLogger = new LoggerSimple((log, s) -> {
            // If this logger fails then we won't have any place to log to anyway.
        });
    }

    @Override
    protected void fallbackLog(Log log, String s) {
        localLogger.logError(getClass().getName(), "Failed to log remotely.");
    }

    @Override
    protected void displayLog(Log log) {
        localLogger.log(log);

        if (remoteLogger != null)
            remoteLogger.log(log);
    }

    @Override
    public void notify(TrafficManager trafficManager) {
        this.remoteLogger = new LoggerRemote(this::fallbackLog, new LogBroadcaster(trafficManager));
    }
}
