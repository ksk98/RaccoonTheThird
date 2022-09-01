package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonServer.Config;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import com.bots.RacoonShared.SocketCommunication.SocketCommunicationOperationBuilder;
import org.json.JSONObject;
import org.springframework.core.env.Environment;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Objects;

public class ServerSocketManager extends Thread {
    private final int port;
    private final String keystorePath, keystorePassword;
    private boolean running = false;

    private final TrafficManager trafficManager;
    private final Logger logger;

    private SSLServerSocket socket = null;

    public ServerSocketManager(Environment environment, Logger logger, TrafficManager trafficManager) {
        String portFromProperties = environment.getProperty("serversocket.port");
        this.port = portFromProperties == null ? Config.defaultPort : Integer.parseInt(portFromProperties);
        this.keystorePath = environment.getProperty("ssl.keystore_path");
        this.keystorePassword = environment.getProperty("ssl.keystore_password");
        this.trafficManager = trafficManager;
        this.logger = logger;
    }

    private ServerSocketFactory getServerSocketFactory() {
        SSLServerSocketFactory ssf;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");;
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");;
            KeyStore ks = KeyStore.getInstance("JKS");;
            char[] passphrase = keystorePassword.toCharArray();

            ks.load(new FileInputStream(keystorePath), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            logger.logError(
                    getClass().getName(),
                    e.toString()
            );
        }

        return null;
    }

    public void stopRunning() {
        running = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.logError(
                        getClass().getName(),
                        e.toString()
                );
            }
        }
        socket = null;
    }

    @Override
    public void run() {
        running = true;
        try {
            socket = (SSLServerSocket) Objects.requireNonNull(getServerSocketFactory()).createServerSocket(port);
        } catch (IOException e) {
            logger.logError(
                    getClass().getName(),
                    e.toString()
            );
            stopRunning();
        }

        while(running) {
            SSLSocket clientSocket = null;
            try {
                clientSocket = (SSLSocket) socket.accept();
                clientSocket.setSoTimeout(Config.clientSocketSOTimeoutMS);
                if (!trafficManager.isRunning())
                    trafficManager.start();

                // Respond with anything so that the handshake will complete
                SocketCommunicationOperationBuilder builder = new SocketCommunicationOperationBuilder()
                        .setWaitForResponse(false);
                trafficManager.queueOperation(trafficManager.getConnection(trafficManager.addConnection(clientSocket)), builder.build());

            } catch (IOException e) {
                logger.logError(
                        getClass().getName(),
                        e.toString()
                );
                try {
                    if (clientSocket != null)
                        trafficManager.removeConnectionForSocket(clientSocket);
                    socket.close();

                } catch (IOException ex) {
                    logger.logError(
                            getClass().getName(),
                            e.toString()
                    );
                }
            } catch (NullPointerException e) {
                logger.logError(
                        getClass().getName(),
                        e.toString()
                );
                stopRunning();
            }
        }
    }
}
