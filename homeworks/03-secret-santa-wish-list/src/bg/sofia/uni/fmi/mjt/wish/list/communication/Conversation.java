package bg.sofia.uni.fmi.mjt.wish.list.communication;

import bg.sofia.uni.fmi.mjt.wish.list.string.stuff.Show;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Conversation {

    public static String getMessage(SocketChannel socketChannel, ByteBuffer buffer) {

        try {
            buffer.clear();
            int readBytes = socketChannel.read(buffer);
            if (readBytes < 0) {
                return null;
            }
            buffer.flip();
            byte[] byteArray = new byte[readBytes];
            buffer.get(byteArray);
            return new String(byteArray, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String sendMessage(SocketChannel socketChannel, String message, ByteBuffer buffer) {

        try {
            message += Show.LINE_SEPARATOR;
            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            return message;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
