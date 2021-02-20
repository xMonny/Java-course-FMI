package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.communication.Conversation;
import bg.sofia.uni.fmi.mjt.wish.list.string.stuff.Show;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class WishListClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final int BUFFER_SIZE = 4096;
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private static void writeToServer(SocketChannel socketChannel, String message) {

        Conversation.sendMessage(socketChannel, message, buffer);
    }

    private static String readFromServer(SocketChannel socketChannel) {

        return Conversation.getMessage(socketChannel, buffer);
    }

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            while (true) {
                String clientMessage = scanner.nextLine();

                writeToServer(socketChannel, clientMessage);
                String serverResponse = readFromServer(socketChannel);

                if (serverResponse == null) {
                    break;
                }
                System.out.print(serverResponse);
                if (serverResponse.trim().equals(Show.USER_DISCONNECTED_MESSAGE)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error in connecting to server", e);
        }
    }
}
