package echoclientserver.net.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EchoServer {
    private static final int SERVER_PORT = 6666;
    private static final int MAX_THREADS = 2;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            Socket clientSocket;

            while(true) {
                clientSocket = serverSocket.accept();
                ClientEventHandler handler = new ClientEventHandler(clientSocket);
                executorService.execute(handler);
            }
            /*Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted client with address " + clientSocket.getInetAddress());

            try(BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("Client text: " + line);
                    out.println("ECHO" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } catch (IOException e) {
            System.out.println("Problem occurred in server socket");
            e.printStackTrace();
        }
    }

}
