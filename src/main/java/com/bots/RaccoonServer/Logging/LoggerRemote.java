package com.bots.RaccoonServer.Logging;

import com.bots.RaccoonServer.SocketCommunication.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Logging.Exceptions.LogException;
import com.bots.RaccoonShared.Logging.Log;

import java.io.IOException;
import java.util.function.BiConsumer;

public class LoggerRemote extends FallbackOverrideableLogger {
    private final IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper;

    public LoggerRemote(BiConsumer<Log, String> fallbackConsumer, IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper) {
        super(fallbackConsumer);
        this.trafficServiceWrapper = trafficServiceWrapper;
    }

    @Override
    protected void displayLog(Log log) throws LogException {
        try {
            trafficServiceWrapper.queueBroadcast(log);
        } catch (IOException e) {
            throw new LogException(e.toString());
        }
    }
}
