package Part1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Simple Client that:
 * 1. Connects to server at 127.0.0.1:5155
 * 2. Asks user for a message
 * 3. Sends the message
 * 4. Closes connection
 */
public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";      // localhost
        int port = 5155;                // Must match server's port

        try (Socket socket = new Socket(host, port)) {              // Connect to server
            System.out.println("Connected to server at " + host + ":" + port);

            // Prepare to send data to server
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);        // true = auto-flush

            // Get message from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your personal message: ");
            String message = scanner.nextLine();

            // Send the message (add newline so server can read it with readLine())
            writer.println(message);
            System.out.println("Message sent: " + message);

            // Cleanup (try-with-resources will close socket automatically)

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}