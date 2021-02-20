package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WishListClient {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String WHITESPACES = "\\s+";
    private static final String ONE_WHITESPACE = " ";

    private static final String DISCONNECT_COMMAND = "disconnect";

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final int BUFFER_SIZE = 4096;

    private static ByteBuffer buffer;

    private static String edit(String line) {
        if (line.isBlank()) {
            line = "unknown";
        } else {
            line = line.trim().replaceAll(WHITESPACES, ONE_WHITESPACE);
        }
        return line;
    }

    private static void sendToServer(SocketChannel socketChannel, String text) {
        try {
            buffer.clear();
            buffer.put((text + LINE_SEPARATOR).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFromServer(SocketChannel socketChannel) {
        try {
            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);

            return new String(byteArray, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

            while (true) {
                String input = scanner.nextLine();
                input = edit(input);

                sendToServer(socketChannel, input);
                String serverResponse = readFromServer(socketChannel);
                if (serverResponse == null) {
                    break;
                }

                System.out.print(serverResponse);
                if (input.equals(DISCONNECT_COMMAND)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
