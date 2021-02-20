package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.scope.ServerThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class WishListServer {

    private static final String SERVER_HOST = "localhost";
    private final int serverPort;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ServerThread serverThread;

    public WishListServer(int port) {
        this.serverPort = port;
    }

    public void start() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, OP_ACCEPT);

            serverThread = new ServerThread(serverSocketChannel, selector);
            serverThread.start();

        } catch (IOException e) {
            shutDown();
            e.printStackTrace();
        }
    }

    private void shutDown() {
        try {
            serverSocketChannel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {

        if (serverSocketChannel != null) {
            serverThread.stopThread();

            while (true) {
                Set<SelectionKey> allKeys = selector.keys();
                //when only key for server socket channel is left
                if (allKeys.size() == 1) {
                    shutDown();
                    break;
                }
            }
        }
    }

    public boolean isOpened() {
        if (serverSocketChannel != null) {
            return serverSocketChannel.isOpen();
        }
        return false;
    }
}
