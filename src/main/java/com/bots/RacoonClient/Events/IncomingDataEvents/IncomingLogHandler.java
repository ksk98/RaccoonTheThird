package com.bots.RacoonClient.Events.IncomingDataEvents;

import com.bots.RacoonClient.Events.IncomingDataEvents.Abstractions.BaseIncomingDataTrafficHandler;
import com.bots.RacoonClient.WindowLogger;
import com.bots.RacoonGeneral.Logging.Log;
import com.bots.RacoonGeneral.Util.SerializationUtil;
import org.json.JSONObject;

import java.io.IOException;

public class IncomingLogHandler extends BaseIncomingDataTrafficHandler {
    @Override
    public void handle(JSONObject response) {
        if (response.getString("operation").equals("log")) {
            try {
                WindowLogger.getInstance().log((Log)SerializationUtil.fromString(response.getString("object")));
            } catch (IOException | ClassNotFoundException e) {
                WindowLogger.getInstance().logError(e.getMessage());
            }
        } else super.handle(response);
    }
}
