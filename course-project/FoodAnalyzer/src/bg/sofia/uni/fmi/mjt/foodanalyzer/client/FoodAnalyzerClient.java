package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import bg.sofia.uni.fmi.mjt.foodanalyzer.client.io.ServerReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.client.io.ServerWriter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class FoodAnalyzerClient {

    private static final String NULL_HOST_MESSAGE = "Detected null host";
    private static final String WRONG_PORT_MESSAGE = "Detected wrong port";
    private static final String WAITING_ERROR_MESSAGE = "Error in waiting client thread";
    private static final String CLIENT_ERROR_MESSAGE = "Error occurred in client";

    private final String host;
    private final int port;

    private static final int MAX_PORT = 65535;

    public FoodAnalyzerClient(String host, int port) {
        if (host == null) {
            throw new IllegalArgumentException(NULL_HOST_MESSAGE);
        }
        if (port < 0 || port > MAX_PORT) {
            throw new IllegalArgumentException(WRONG_PORT_MESSAGE);
        }
        this.host = host;
        this.port = port;
    }

    private void waitThread(ClientThread thread) {
        while (thread.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(WAITING_ERROR_MESSAGE);
            }
        }
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(host, port));

            ClientThread clientThread = new ClientThread(socketChannel);
            clientThread.start();

            waitThread(clientThread);

        } catch (IOException e) {
            throw new IllegalStateException(CLIENT_ERROR_MESSAGE, e);
        }
    }
}
