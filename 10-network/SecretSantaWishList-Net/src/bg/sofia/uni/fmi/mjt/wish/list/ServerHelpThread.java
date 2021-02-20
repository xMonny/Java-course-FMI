package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static bg.sofia.uni.fmi.mjt.wish.list.WishListServer.executorService;

public class ServerHelpThread extends Thread {

    private final ServerSocket serverSocket;

    public ServerHelpThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket;
            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
