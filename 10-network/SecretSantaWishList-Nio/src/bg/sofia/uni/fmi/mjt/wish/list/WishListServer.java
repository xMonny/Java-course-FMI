package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class WishListServer {

    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;
    private final int serverPort;

    public WishListServer(int port) {
        this.serverPort = port;
    }

    private static final Map<String, Set<String>> studentPresents = new HashMap<>();

    static Map<String, Set<String>> getStudentPresents() {
        return studentPresents;
    }

    static void addPresent(String studentName, String present) {
        Set<String> updatedPresents = ((existStudentName(studentName))
                ? studentPresents.get(studentName) : new LinkedHashSet<>());
        updatedPresents.add(present);
        studentPresents.put(studentName, updatedPresents);
    }

    static void remove(String studentName) {
        studentPresents.remove(studentName);
    }

    static boolean existStudentName(String studentName) {
        return studentPresents.containsKey(studentName);
    }

    static Set<String> getPresents(String studentName) {
        return studentPresents.get(studentName);
    }

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public void start() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerHelpThread helpThread = new ServerHelpThread(selector, buffer);
        helpThread.start();
    }

    public void stop() {
        try {
            serverSocketChannel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
