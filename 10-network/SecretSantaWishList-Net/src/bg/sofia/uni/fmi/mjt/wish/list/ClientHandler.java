package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ClientHandler implements Runnable {

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

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private static void registerPresent(String studentName, String present, PrintWriter out) {

        if (WishListServer.existsStudentName(studentName)) {
            Set<String> studentPresents = WishListServer.getPresents(studentName);
            if (studentPresents.contains(present)) {
                out.format(MESSAGE_ALREADY_SUBMITTED_GIFT, studentName).println();
            } else {
                WishListServer.addPresent(studentName, present);
                out.format(MESSAGE_SUBMITTED_SUCCESSFULLY, present, studentName).println();
            }
        } else {
            WishListServer.addPresent(studentName, present);
            out.format(MESSAGE_SUBMITTED_SUCCESSFULLY, present, studentName).println();
        }
    }

    private static String getRandomName(Map<String, Set<String>> studentPresents) {
        List<String> studentNames = new ArrayList<>(studentPresents.keySet());
        Random rand = new Random();
        return studentNames.get(rand.nextInt(studentNames.size()));
    }

    private static void removeStudent(String studentName) {
        WishListServer.remove(studentName);
    }

    private static void sendMessage(PrintWriter out, String message) {
        out.println(message);
    }

    private static void sendPostWishInformation(PrintWriter out, String[] splitted, String clientMessage) {
        String command = splitted[0];
        String studentName = splitted[1];
        String present = clientMessage.substring(command.length() + studentName.length() + 2);
        registerPresent(studentName, present, out);
    }

    private static String formatStudentPresents(String studentName) {
        return "[ "
                + studentName
                + ": "
                + WishListServer.getPresents(studentName).toString()
                + " ]";
    }

    private static void printStudentPresents(PrintWriter out) {
        if (WishListServer.getStudentPresents().isEmpty()) {
            sendMessage(out, MESSAGE_NO_STUDENTS);
        } else {
            String selectedName = getRandomName(WishListServer.getStudentPresents());
            String response = formatStudentPresents(selectedName);
            sendMessage(out, response);
            removeStudent(selectedName);
        }
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (WishListServer.executorService.isShutdown()) {
                    break;
                }
                String[] splitted = line.split(BY_ONE_WHITESPACE);
                String command = splitted[0];

                if ((splitted.length != 1 && !command.equals(POST_WISH_COMMAND))
                        || (splitted.length < 3 && command.equals(POST_WISH_COMMAND))) {
                    sendMessage(out, UNKNOWN_COMMAND);
                    continue;
                }

                switch (command) {
                    case POST_WISH_COMMAND -> sendPostWishInformation(out, splitted, line);
                    case GET_WISH_COMMAND -> printStudentPresents(out);
                    case DISCONNECT_COMMAND -> sendMessage(out, MESSAGE_DISCONNECTED);
                    default -> out.println(UNKNOWN_COMMAND);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
