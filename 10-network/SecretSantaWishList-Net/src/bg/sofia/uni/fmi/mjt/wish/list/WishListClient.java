package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WishListClient {

    private static final String WHITESPACES = "\\s+";
    private static final String ONE_WHITESPACE = " ";

    private static final String DISCONNECT_COMMAND = "disconnect";

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;

    private static String edit(String line) {
        return line.trim().replaceAll(WHITESPACES, ONE_WHITESPACE);
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)) {

            while (true) {
                String input = scanner.nextLine(); //read text from System.in
                input = edit(input);

                out.println(input); //send edited text to server
                String serverResponse = br.readLine();
                if (serverResponse == null) {
                    break;
                }
                System.out.println(serverResponse); //print received text from server
                if (input.equals(DISCONNECT_COMMAND)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
