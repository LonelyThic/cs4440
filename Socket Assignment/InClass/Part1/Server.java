import java.io.*;
import java.net.*;


public class Server {
    public static void main(String[] args) {
        // The port number the server will listen on
        int port = 5155;

        try (ServerSocket serverSocket = new ServerSocket(port)) {     // Create server socket that listens on the port
            System.out.println("Server started on port " + port);       // Inform user that server is ready
            System.out.println("Waiting for a client to connect...");   // Show waiting status

            Socket clientSocket = serverSocket.accept();                // Block and wait until a client connects
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Get input stream from the client
            InputStream in = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // Read one line sent by the client
            String message = reader.readLine();

            // Display the received message
            if (message != null) {
                System.out.println("Received message: " + message);
            } else {
                System.out.println("Client sent empty message or closed connection.");
            }

            // Clean up
            reader.close();
            clientSocket.close();
            System.out.println("Connection closed. Server will now exit.");

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}