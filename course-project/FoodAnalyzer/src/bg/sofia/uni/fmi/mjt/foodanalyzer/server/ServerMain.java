package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        FoodAnalyzerServer server = new FoodAnalyzerServer(5555);
        server.start();
        Thread.sleep(10000);
        server.stop();
    }
}
