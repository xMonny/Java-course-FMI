package bg.sofia.uni.fmi.mjt.foodanalyzer.client.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public final class ServerReader {

    private static final String NULL_SOCKET_CHANNEL_MESSAGE = "Detected null socket channel";
    private static final String NULL_BUFFER_MESSAGE = "Detected null buffer";
    private static final String CLOSE_CHANNEL_ERROR_MESSAGE = "Error occurred in closing channel";

    private final SocketChannel socketChannel;
    private final ByteBuffer buffer;

    public ServerReader(SocketChannel socketChannel, ByteBuffer buffer) {
        if (socketChannel == null) {
            throw new IllegalArgumentException(NULL_SOCKET_CHANNEL_MESSAGE);
        }
        if (buffer == null) {
            throw new IllegalArgumentException(NULL_BUFFER_MESSAGE);
        }
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }

    private void closeSocketChannel() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            throw new IllegalStateException(CLOSE_CHANNEL_ERROR_MESSAGE, e);
        }
    }

    public String read() throws IOException {
        buffer.clear();
        int readBytes = socketChannel.read(buffer);
        if (readBytes < 0) {
            closeSocketChannel();
            throw new ClosedChannelException();
        }
        buffer.flip();
        byte[] byteArray = new byte[readBytes];
        buffer.get(byteArray);

        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
