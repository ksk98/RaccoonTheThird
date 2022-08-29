package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonServer.Config;
import com.bots.RacoonShared.Logging.Loggers.Logger;
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
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "passphrase".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            logger.logError(e.getMessage());
        }

        return null;
    }

    public void stopRunning() {
        running = false;
    }

    @Override
    public void run() {
        running = true;

        while(running) {
            try {
                SSLServerSocket socket = (SSLServerSocket) Objects.requireNonNull(getServerSocketFactory()).createServerSocket(port);

                trafficManager.addConnection((SSLSocket) socket.accept());
                if (!trafficManager.isRunning())
                    trafficManager.start();

            } catch (IOException e) {
                logger.logError(e.getMessage());
            } catch (NullPointerException e) {
                logger.logError(e.getMessage());
                stopRunning();
            }
        }
    }
}
