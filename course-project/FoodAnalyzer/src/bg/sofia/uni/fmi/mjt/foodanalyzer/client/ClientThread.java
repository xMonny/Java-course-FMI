package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

import bg.sofia.uni.fmi.mjt.foodanalyzer.client.io.ServerReader;
import bg.sofia.uni.fmi.mjt.foodanalyzer.client.io.ServerWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientThread extends Thread {

    private static final String CLOSE_SOCKET_ERROR_MESSAGE = "Error occurred in closing socket channel";
    private static final String WRITING_FROM_SERVER_ERROR_MESSAGE = "Error occurred while writing to server";
    private static final String READING_FROM_SERVER_ERROR_MESSAGE = "Error occurred while reading from server";
    private static final String DISCONNECTED_MESSAGE = "Disconnected from server";

    private static final int BUFFER_SIZE = 50000;

    private final SocketChannel socketChannel;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public ClientThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    private void closeSocketChannel(SocketChannel socketChannel) {
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.out.println(CLOSE_SOCKET_ERROR_MESSAGE);
            }
        }
    }

    private void writeToServer(SocketChannel socketChannel, String message) {
        try {
            ServerWriter serverWriter = new ServerWriter(socketChannel, buffer);
            serverWriter.write(message);
        } catch (IOException e) {
            System.out.println(WRITING_FROM_SERVER_ERROR_MESSAGE);
            e.printStackTrace();
            closeSocketChannel(socketChannel);
        }
    }

    private String readFromServer(SocketChannel socketChannel) {
        try {
            ServerReader serverReader = new ServerReader(socketChannel, buffer);
            return serverReader.read().trim();
        } catch (IOException e) {
            System.out.println(READING_FROM_SERVER_ERROR_MESSAGE);
            e.printStackTrace();
            closeSocketChannel(socketChannel);
            return null;
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String clientMessage = scanner.nextLine();
            writeToServer(socketChannel, clientMessage);
            String serverResponse = readFromServer(socketChannel);
            if (serverResponse == null) {
                break;
            }
            System.out.println(serverResponse);
            if (serverResponse.equals(DISCONNECTED_MESSAGE)) {
                break;
            }
        }
    }
}
