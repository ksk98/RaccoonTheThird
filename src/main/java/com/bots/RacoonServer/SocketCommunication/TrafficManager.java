package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonShared.IncomingDataHandlers.IncomingDataTrafficHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.SocketCommunication.CommunicationUtil;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperation;
import org.json.JSONObject;
import org.springframework.data.util.Pair;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

public class TrafficManager extends Thread {
    private boolean running = false;
    private final Logger logger;

    private final HashMap<Integer, SocketConnection> connections;
    private int nextSubscriberId = 0;

    private final Map<Integer, Pair<SocketConnection, SocketCommunicationOperation>> individualOperations;
    private final Queue<SocketCommunicationOperation> broadcasts;
    private final Queue<Integer> individualOperationIdQueue;
    private int individualOperationNextId;

    private final IncomingDataTrafficHandler trafficHandlerChain;

    public TrafficManager(Logger logger, IncomingDataTrafficHandler trafficHandlerChain) {
        this.logger = logger;
        this.connections = new HashMap<>();
        this.individualOperations = new HashMap<>();
        this.broadcasts = new LinkedList<>();
        this.individualOperationIdQueue = new LinkedList<>();
        this.individualOperationNextId = 0;
        this.trafficHandlerChain = trafficHandlerChain;
    }

    public void queueOperation(SocketConnection connection, SocketCommunicationOperation operation) {
        individualOperations.put(individualOperationNextId, Pair.of(connection, operation));
        individualOperationIdQueue.add(individualOperationNextId);
        individualOperationNextId += 1;
    }

    private void finaliseOperationForResponse(JSONObject response) {
        int id = response.getInt("server_operation_id");
        SocketCommunicationOperation operation = individualOperations.remove(id).getSecond();
        operation.getOnResponseReceived().accept(response);

        if (individualOperations.isEmpty())
            individualOperationNextId = 0;
    }

    private void removeOperation(int id) {
        individualOperations.remove(id);

        if (individualOperations.isEmpty())
            individualOperationNextId = 0;
    }

    public void queueBroadcast(SocketCommunicationOperation operation) {
        broadcasts.add(operation);
    }

    public void stopRunning() {
        this.running = false;
    }

    public SocketConnection getConnection(int id) {
        return connections.get(id);
    }

    public int addConnection(SSLSocket socket) throws IOException {
        connections.put(nextSubscriberId, new SocketConnection(socket, nextSubscriberId));
        nextSubscriberId += 1;

        return nextSubscriberId - 1;
    }

    public void removeConnection(int id) {
        connections.remove(id);

        if (connections.isEmpty())
            nextSubscriberId = 0;
    }

    public void removeConnectionForSocket(SSLSocket socket) {
        for (Integer connectionId: connections.keySet()) {
            if (connections.get(connectionId).socket.equals(socket)) {
                removeConnection(connectionId);
                return;
            }
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if (!individualOperationIdQueue.isEmpty()) {
                Integer idToSend = individualOperationIdQueue.poll();
                Pair<SocketConnection, SocketCommunicationOperation> individualOperation = individualOperations.get(idToSend);
                JSONObject request = individualOperation.getSecond().getRequest().append("server_operation_id", idToSend);

                boolean aborted = false;
                try {
                    CommunicationUtil.sendTo(individualOperation.getFirst().out, request);
                } catch (IOException e) {
                    individualOperation.getSecond().getOnErrorEncountered().accept(e.toString());
                    removeOperation(idToSend);
                    aborted = true;
                }

                if (!aborted) {
                    if (!individualOperation.getSecond().waitForResponse())
                        removeOperation(idToSend);

                    if (individualOperation.getFirst().out.checkError()) {
                        individualOperations.get(idToSend).getSecond().getOnErrorEncountered().accept("PrintWriter failed to send request: " + request);
                        removeOperation(idToSend);
                    }
                }
            }

            try {
                for (SocketConnection connection : connections.values()) {
                    if (connection.isExpired()) {
                        logger.logInfo("Removed expired client connection.");
                        removeConnection(connection.getId());
                        break;
                    }

                    JSONObject incomingData;
                    try {
                        incomingData = new JSONObject(CommunicationUtil.readUntilEndFrom(connection.in));
                    } catch (SocketTimeoutException ignored) {
                        continue;
                    } catch (IOException e) {
                        logger.logError(e.toString());
                        continue;
                    }

                    incomingData.append("connection_id", connection.in.id);
                    if (incomingData.has("server_operation_id")) {
                        finaliseOperationForResponse(incomingData);
                    } else if (incomingData.has("operation")) {
                        trafficHandlerChain.handle(incomingData);
                    } else {
                        logger.logInfo("Data was received from socket stream but could not be handled.");
                    }
                }
            } catch (ConcurrentModificationException ignored) {}

            if (!broadcasts.isEmpty()) {
                SocketCommunicationOperation message = broadcasts.poll();

                for (SocketConnection connection: connections.values()) {
                    if (connection.isAuthenticated()) {
                        try {
                            CommunicationUtil.sendTo(connection.out, message.getRequest());
                        } catch (IOException e) {
                            logger.logError(e.toString());
//                            continue;
                        }
                    }
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
