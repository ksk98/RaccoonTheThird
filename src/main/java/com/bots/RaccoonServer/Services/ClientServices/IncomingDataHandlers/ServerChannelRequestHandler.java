package com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers;

import com.bots.RaccoonServer.Services.DiscordServices.BotIntelService;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONOperationHandler;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import com.bots.RaccoonShared.SocketCommunication.SocketOperationIdentifiers;
import org.json.JSONObject;

import java.io.IOException;

public class ServerChannelRequestHandler extends JSONOperationHandler {
    private final ILogger logger;
    private final IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper;
    private final BotIntelService botIntelService;

    public ServerChannelRequestHandler(ILogger logger, IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper,
                                       BotIntelService botIntelService) {
        super(SocketOperationIdentifiers.REQUEST_SERVER_CHANNEL_LIST);
        this.logger = logger;
        this.trafficServiceWrapper = trafficServiceWrapper;
        this.botIntelService = botIntelService;
    }

    @Override
    public void consume(JSONObject data) {
        try {
            trafficServiceWrapper.queueOperation(
                    trafficServiceWrapper.getConnectionForId(data.getInt("connection_id")),
                    botIntelService.getServerChannelList()
            );
        } catch (IOException e) {
            logger.logError(getClass().getName(), "Could not send server-channel list to client: " + e);
        }
    }
}
