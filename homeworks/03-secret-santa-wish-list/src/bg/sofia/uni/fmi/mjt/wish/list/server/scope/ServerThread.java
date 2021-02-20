package bg.sofia.uni.fmi.mjt.wish.list.server.scope;

import bg.sofia.uni.fmi.mjt.wish.list.string.stuff.Show;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_READ;

public class ServerThread extends Thread {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ServerActions serverActions = new ServerActions();
    private boolean shouldStop = false;

    public ServerThread(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    private void accept(SelectionKey key) {

        try {
            if (serverSocketChannel != null) {
                SocketChannel accepted = serverSocketChannel.accept();
                accepted.configureBlocking(false);
                accepted.register(selector, OP_READ);
            } else {
                key.cancel();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        this.shouldStop = true;
    }

    @Override
    public void run() {

        try {
            while (selector.isOpen()) {

                int readyClients = selector.select();
                if (readyClients == 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectedKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isValid() && key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        if (shouldStop) {
                            serverActions.answer(socketChannel, Show.DISCONNECT_COMMAND);
                            socketChannel.close();
                            continue;
                        }
                        String clientMessage = serverActions.getClientMessage(socketChannel);
                        if (clientMessage == null) {
                            socketChannel.close();
                            continue;
                        }
                        serverActions.answer(socketChannel, clientMessage);
                        String lastServerResponse = serverActions.getLastServerResponse();
                        if (lastServerResponse.equals(Show.USER_DISCONNECTED_MESSAGE)) {
                            socketChannel.close();
                        }
                    } else if (key.isValid() && key.isAcceptable() && !shouldStop) {
                        accept(key);
                    }
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocketChannel.close();
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
