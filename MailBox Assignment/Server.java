
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {

    private static final int PORT = 5000;

    // Shared mailbox
    public static BlockingQueue<String> mailbox =
            new LinkedBlockingQueue<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected.");

                ClientHandler handler =
                        new ClientHandler(clientSocket);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}