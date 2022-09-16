package com.bots.RaccoonServer.Logging;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreateListener;
import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.SocketCommunication.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Logging.Log;
import com.bots.RaccoonShared.Logging.Loggers.Logger;

public class RacoonLogger extends Logger implements GenericOnCreateListener<IOutboundTrafficServiceUtilityWrapper> {
    private final Logger localLogger;
    private Logger remoteLogger = null;

    public RacoonLogger(GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher) {
        trafficServiceWrapperCreationPublisher.subscribe(this);
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
    public void notify(IOutboundTrafficServiceUtilityWrapper created) {
        remoteLogger = new LoggerRemote(this::fallbackLog, created);
    }
}
