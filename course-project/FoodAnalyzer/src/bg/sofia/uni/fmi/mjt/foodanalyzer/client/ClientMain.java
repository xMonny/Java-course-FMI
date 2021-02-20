package bg.sofia.uni.fmi.mjt.foodanalyzer.client;

public class ClientMain {

    private static final String HOST = "localhost";
    private static final int PORT = 5555;

    public static void main(String[] args) {
        FoodAnalyzerClient client = new FoodAnalyzerClient(HOST, PORT);
        client.start();
    }
}
