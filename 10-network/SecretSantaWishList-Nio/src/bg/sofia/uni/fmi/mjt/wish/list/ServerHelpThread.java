package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ServerHelpThread extends Thread {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String POST_WISH_COMMAND = "post-wish";
    private static final String GET_WISH_COMMAND = "get-wish";
    private static final String DISCONNECT_COMMAND = "disconnect";
    private static final String UNKNOWN_COMMAND = "[ Unknown command ]";
    private static final String MESSAGE_ALREADY_SUBMITTED_GIFT =
            "[ The same gift for student %s was already submitted ]";
    private static final String MESSAGE_SUBMITTED_SUCCESSFULLY = "[ Gift %s for student %s submitted successfully ]";
    private static final String MESSAGE_DISCONNECTED = "[ Disconnected from server ]";
    private static final String MESSAGE_NO_STUDENTS = "[ There are no students present in the wish list ]";

    private static final String BY_ONE_WHITESPACE = " ";

    private static Selector selector;
    private static ByteBuffer buffer;

    public ServerHelpThread(Selector selector, ByteBuffer buffer) {
        ServerHelpThread.selector = selector;
        ServerHelpThread.buffer = buffer;
    }

    private static void sendMessage(SocketChannel socketChannel, String message) {
        try {
            message += LINE_SEPARATOR;
            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void registerPresent(String studentName, String present, SocketChannel socketChannel) {

        if (WishListServer.existStudentName(studentName)) {
            Set<String> studentPresents = WishListServer.getPresents(studentName);
            if (studentPresents.contains(present)) {
                sendMessage(socketChannel, MESSAGE_ALREADY_SUBMITTED_GIFT.formatted(studentName));
                return;
            }
        }
        WishListServer.addPresent(studentName, present);
        sendMessage(socketChannel, MESSAGE_SUBMITTED_SUCCESSFULLY.formatted(present, studentName));
    }

    private static String getRandomName(Map<String, Set<String>> studentPresents) {
        List<String> studentNames = new ArrayList<>(studentPresents.keySet());
        Random rand = new Random();
        return studentNames.get(rand.nextInt(studentNames.size()));
    }

    private static void removeStudent(String studentName) {
        WishListServer.remove(studentName);
    }

    private static String formatStudentPresents(String studentName) {
        return "[ "
                + studentName
                + ": "
                + WishListServer.getPresents(studentName).toString()
                + " ]";
    }

    private static void printStudentPresents(SocketChannel socketChannel) {
        if (WishListServer.getStudentPresents().isEmpty()) {
            sendMessage(socketChannel, MESSAGE_NO_STUDENTS);
        } else {
            String selectedName = getRandomName(WishListServer.getStudentPresents());
            String response = formatStudentPresents(selectedName);
            sendMessage(socketChannel, response);
            removeStudent(selectedName);
        }
    }

    private static void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel accept = serverSocketChannel.accept();
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getClientMessage(SocketChannel socketChannel) {
        try {
            buffer.clear();
            int r = socketChannel.read(buffer);
            if (r < 0) {
                socketChannel.close();
                return null;
            }
            buffer.flip();

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);

            return new String(byteArray, StandardCharsets.UTF_8).trim();

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void sendPostWishInformation(SocketChannel socketChannel, String[] splitted, String clientMessage) {
        String command = splitted[0];
        String studentName = splitted[1];
        String present = clientMessage.substring(command.length() + studentName.length() + 2);
        registerPresent(studentName, present, socketChannel);
    }

    private static void answer(SocketChannel socketChannel, String clientMessage) {
        String[] splitted = clientMessage.split(BY_ONE_WHITESPACE);
        String command = splitted[0];

        if ((splitted.length != 1 && !command.equals(POST_WISH_COMMAND))
                || (splitted.length < 3 && command.equals(POST_WISH_COMMAND))) {
            sendMessage(socketChannel, UNKNOWN_COMMAND);
            return;
        }

        switch (command) {
            case POST_WISH_COMMAND -> sendPostWishInformation(socketChannel, splitted, clientMessage);
            case GET_WISH_COMMAND -> printStudentPresents(socketChannel);
            case DISCONNECT_COMMAND -> sendMessage(socketChannel, MESSAGE_DISCONNECTED);
            default -> sendMessage(socketChannel, UNKNOWN_COMMAND);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        System.out.println(socketChannel.getRemoteAddress());
                        String clientMessage = getClientMessage(socketChannel);
                        if (clientMessage == null) {
                            continue;
                        }
                        answer(socketChannel, clientMessage);
                    } else if (key.isAcceptable()) {
                        accept(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
