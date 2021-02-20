package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishListServer {

    private static final int MAX_THREADS = 20;

    private ServerSocket serverSocket;
    private final int serverPort;
    static ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

    private static final Map<String, Set<String>> studentPresents = Collections.synchronizedMap(new HashMap<>());

    public WishListServer(int port) {
        serverPort = port;
    }

    static boolean existsStudentName(String studentName) {
        return studentPresents.containsKey(studentName);
    }

    static Map<String, Set<String>> getStudentPresents() {
        return studentPresents;
    }

    static void addPresent(String studentName, String present) {
        Set<String> updatedPresents = (existsStudentName(studentName))
                ? studentPresents.get(studentName) : new LinkedHashSet<>();
        updatedPresents.add(present);
        studentPresents.put(studentName, updatedPresents);
    }

    static void remove(String studentName) {
        studentPresents.remove(studentName);
    }

    static Set<String> getPresents(String studentName) {
        return studentPresents.get(studentName);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerHelpThread serverHelpThread = new ServerHelpThread(serverSocket);
        serverHelpThread.start();
    }

    public void stop() {
        try {
            executorService.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
