package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Command;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.CommandExtractor;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.channel.ChannelReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.channel.ChannelWriter;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.storage.FoodStorageAccessor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerThread extends Thread {

    private static final String ACCEPTING_CLIENTS_ERROR_MESSAGE = "Error occurred in accepting clients";
    private static final String NULL_MESSAGE_TO_SEND = "Detected null message to be send";
    private static final String WRITE_IN_BUFFER_ERROR_MESSAGE = "Error occurred in writing in channel buffer";
    private static final String SOCKET_CLOSING_ERROR_MESSAGE = "Error occurred in closing channel";
    private static final String SELECTING_KEYS_ERROR_MESSAGE = "Error occurred in selecting keys";
    private static final String NULL_SELECTOR_MESSAGE = "Detected null selector";
    private static final String READ_FROM_CLIENT_ERROR_MESSAGE = "Error occurred while reading from client";
    private static final String DISCONNECT_COMMAND = "disconnect";

    private static final int BUFFER_SIZE = 50000;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private final Selector selector;
    private boolean shouldStop = false;

    public ServerThread(Selector selector) {
        if (selector == null) {
            throw new IllegalArgumentException(NULL_SELECTOR_MESSAGE);
        }
        this.selector = selector;
    }

    private void accept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        try {
            if (serverSocketChannel != null) {
                SocketChannel acceptedChannel = serverSocketChannel.accept();
                acceptedChannel.configureBlocking(false);
                acceptedChannel.register(selector, SelectionKey.OP_READ);
            } else {
                key.cancel();
            }
        } catch (IOException e) {
            System.err.println(ACCEPTING_CLIENTS_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void sendMessage(SocketChannel socketChannel, String message) {
        if (message == null) {
            throw new IllegalArgumentException(NULL_MESSAGE_TO_SEND);
        }
        try {
            ChannelWriter channelWriter = new ChannelWriter(socketChannel, buffer);
            channelWriter.write(message);
        } catch (IOException e) {
            System.err.println(WRITE_IN_BUFFER_ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String read(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        ChannelReader channelReader = new ChannelReader(socketChannel, buffer);
        String clientMessage;
        try {
            clientMessage = channelReader.read();
        } catch (IOException e) {
            System.err.println(READ_FROM_CLIENT_ERROR_MESSAGE);
            key.cancel();
            closeSocketChannel(socketChannel);
            return null;
        }
        return clientMessage.trim();
    }

    private void execute(SelectionKey key, String message) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        if (shouldStop) {
            message = DISCONNECT_COMMAND;
        }
        FoodStorageAccessor accessor = new FoodStorageAccessor();
        CommandExtractor commandExtractor = new CommandExtractor(accessor, message);
        Command command = commandExtractor.extract();

        String response = command.getResponse();
        sendMessage(socketChannel, response);
    }

    private void closeSocketChannel(SocketChannel socketChannel) {
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.err.println(SOCKET_CLOSING_ERROR_MESSAGE);
            }
        }
    }

    private int numberOfReadyKeys() {
        int readyClients = 0;
        try {
            readyClients = selector.select();
        } catch (IOException e) {
            System.err.println(SELECTING_KEYS_ERROR_MESSAGE);
        }
        return readyClients;
    }

    @Override
    public void run() {
        while (selector.isOpen()) {
            if (numberOfReadyKeys() == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey currentKey = keyIterator.next();
                if (currentKey.isValid() && currentKey.isReadable()) {
                    String clientMessage = read(currentKey);
                    execute(currentKey, clientMessage);
                } else if (currentKey.isValid() && currentKey.isAcceptable() && !shouldStop) {
                    accept(currentKey);
                }
                keyIterator.remove();
            }
        }
    }

    public void shutDown() {
        this.shouldStop = true;
    }
}
