package bg.sofia.uni.fmi.mjt.foodanalyzer.client.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.foodanalyzer.server.content.text.Label.LINE_SEPARATOR;

public final class ServerWriter {

    private static final String NULL_SOCKET_CHANNEL_MESSAGE = "Detected null socket channel";
    private static final String NULL_BUFFER_MESSAGE = "Detected null buffer";

    private final SocketChannel socketChannel;
    private final ByteBuffer buffer;

    public ServerWriter(SocketChannel socketChannel, ByteBuffer buffer) {
        if (socketChannel == null) {
            throw new IllegalArgumentException(NULL_SOCKET_CHANNEL_MESSAGE);
        }
        if (buffer == null) {
            throw new IllegalArgumentException(NULL_BUFFER_MESSAGE);
        }
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }

    public String write(String message) throws IOException {
        message += LINE_SEPARATOR;
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);

        return message;
    }
}
