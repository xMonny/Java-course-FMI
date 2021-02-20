package echoclientserver.net.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6666;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server");

            while (true) {
                System.out.println("Enter message: ");
                String line = scanner.nextLine();

                if (line.equals("quit")) {
                    break;
                }
                System.out.println("Sending text <" + line + "> to server");
                out.println(line);

                System.out.println("Server responsed: " + "<" + br.readLine() + ">");
            }

        } catch (IOException e) {
            System.out.println("Problem occurred in client socket");
            e.printStackTrace();
        }
    }
}
