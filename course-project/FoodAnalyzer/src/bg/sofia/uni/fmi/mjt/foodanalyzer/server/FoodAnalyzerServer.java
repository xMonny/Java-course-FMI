package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class FoodAnalyzerServer {

    private static final String WRONG_SERVER_PORT_MESSAGE = "Detected wrong server port";
    private static final String SERVER_OPEN_ERROR_MESSAGE = "Error occurred in preparation for server opening";
    private static final String SELECTOR_OPEN_ERROR_MESSAGE = "Error occurred in preparation for selector opening";
    private static final String SERVER_CLOSE_ERROR_MESSAGE = "Error occurred in closing server channel";
    private static final String SELECTOR_CLOSE_ERROR_MESSAGE = "Error occurred in closing selector";
    private static final String START_SERVER_MESSAGE = "Start server!";

    private static final String SERVER_HOST = "localhost";
    private static final int MAX_PORT = 65535;

    private final int serverPort;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ServerThread serverThread;

    public FoodAnalyzerServer(int serverPort) {
        if (serverPort < 0 || serverPort > MAX_PORT) {
            throw new IllegalArgumentException(WRONG_SERVER_PORT_MESSAGE);
        }
        this.serverPort = serverPort;
    }

    public boolean isStarted() {
        return serverSocketChannel != null && serverSocketChannel.isOpen();
    }

    public void start() {
        openServerSocketChannel();
        openSelector();
        startServerThread();
        System.out.println(START_SERVER_MESSAGE);
    }

    public void stop() {
        if (serverSocketChannel != null) {
            stopServerThread();

            while (true) {
                Set<SelectionKey> allKeys = selector.keys();
                //when is only server socket channel
                if (allKeys.size() == 1) {
                    closeServerSocketChannel();
                    closeSelector();
                    break;
                }
            }
        }

    }

    private void openServerSocketChannel() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);
        } catch (IOException e) {
            closeServerSocketChannel();
            System.err.println(SERVER_OPEN_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openSelector() {
        try {
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            closeSelector();
            System.err.println(SELECTOR_OPEN_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void startServerThread() {
        serverThread = new ServerThread(selector);
        serverThread.start();
    }

    private void stopServerThread() {
        if (serverThread != null) {
            serverThread.shutDown();
        }
    }

    private void closeServerSocketChannel() {
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                System.out.println(SERVER_CLOSE_ERROR_MESSAGE);
            }
        }
    }

    private void closeSelector() {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                System.out.println(SELECTOR_CLOSE_ERROR_MESSAGE);
            }
        }
    }
}
