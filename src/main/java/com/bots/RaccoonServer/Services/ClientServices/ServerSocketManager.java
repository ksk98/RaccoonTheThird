package com.bots.RaccoonServer.Services.ClientServices;

import com.bots.RaccoonServer.Config;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.TrafficService;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import org.springframework.core.env.Environment;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Objects;

public class ServerSocketManager extends Thread {
    private final int port;
    private final String keystorePath, keystorePassword;
    private boolean running = false;

    private final TrafficService trafficService;
    private final ILogger logger;

    private SSLServerSocket socket = null;

    public ServerSocketManager(Environment environment, ILogger logger, TrafficService trafficService) {
        String portFromProperties = environment.getProperty("serversocket.port");
        this.port = portFromProperties == null ? Config.defaultPort : Integer.parseInt(portFromProperties);
        this.keystorePath = environment.getProperty("ssl.keystore_path");
        this.keystorePassword = environment.getProperty("ssl.keystore_password");
        this.trafficService = trafficService;
        this.logger = logger;
    }

    private ServerSocketFactory getServerSocketFactory() {
        SSLServerSocketFactory ssf;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] passphrase = keystorePassword.toCharArray();

            ks.load(new FileInputStream(keystorePath), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            logger.logError(getClass().getName(), e.toString());
        }

        return null;
    }

    public void stopRunning() {
        running = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.logError(getClass().getName(), e.toString());
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
            logger.logError(getClass().getName(), e.toString());
            stopRunning();
        }

        while (running) {
            SSLSocket clientSocket = null;
            try {
                clientSocket = (SSLSocket) socket.accept();
                clientSocket.setSoTimeout(Config.clientSocketSOTimeoutMS);
                trafficService.addConnection(clientSocket);

                synchronized (trafficService) {
                    trafficService.notify();
                }
            } catch (IOException e) {
                logger.logError(getClass().getName(), e.toString());
                try {
                    if (clientSocket != null)
                        trafficService.removeConnectionForSocket(clientSocket);
                    socket.close();

                } catch (IOException ex) {
                    logger.logError(getClass().getName(), e.toString());
                }
            } catch (NullPointerException e) {
                logger.logError(getClass().getName(), e.toString());
                stopRunning();
            }
        }
    }
}
