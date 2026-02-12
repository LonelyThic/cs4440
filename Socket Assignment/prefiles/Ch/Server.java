package Socket Assignment.prefiles.Ch;

import java.io.*;                  // Import classes for input and output streams
import java.net.*;                // Import classes for networking (ServerSocket, Socket)
import java.util.*;               // Import classes for HashMap and ArrayList

public class Server {

    // This HashMap stores the mailbox:
    // Key = addressee
    // Value = list of messages for that addressee
    private static HashMap<String, ArrayList<String>> mailboxes = new HashMap<>();

    // This controls the mailbox size:
    // 0 means unlimited mailbox
    // Any positive number means limited mailbox
    private static int mailboxSizeLimit = 5; // Change to 0 for unlimited

    public static void main(String[] args) {
        ServerSocket serverSocket = null;   // This listens for client connections

        try {
            // Create a ServerSocket that listens on port 5155
            serverSocket = new ServerSocket(5155);

            // Print a message to show the server is running
            System.out.println("Server is running and waiting for connections...");

            // Run forever so the server keeps accepting clients
            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();

                // Print a message when a client connects
                System.out.println("Client connected.");

                // Create a BufferedReader to read data from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Read one line (one message) from the client
                String message = in.readLine();

                // Print the received message
                System.out.println("Received: " + message);

                // Process and store the message
                processMessage(message);

                // Close the client connection
                clientSocket.close();

                // Print a message to show the connection is closed
                System.out.println("Client disconnected.\n");
            }

        } catch (IOException e) {
            // Print an error message if something goes wrong
            System.out.println("Server error: " + e.getMessage());
        } finally {
            try {
                // Close the server socket if it was opened
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                // Print an error message if closing fails
                System.out.println("Error closing server: " + e.getMessage());
            }
        }
    }

    // This method accepts a String message and figures out its format
    private static void processMessage(String message) {
        // Split the message using commas
        String[] parts = message.split(",");

        // Trim spaces from each part
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String addressee;  // Who the message is for
        String sender;     // Who sent the message
        String content;    // The message text

        // If the message has only one part: "Hello"
        if (parts.length == 1) {
            addressee = "UNKNOWN";
            sender = "UNKNOWN";
            content = parts[0];
        }
        // If the message has two parts: "1, Hello"
        else if (parts.length == 2) {
            addressee = parts[0];
            sender = "UNKNOWN";
            content = parts[1];
        }
        // If the message has three or more parts: "1,2, Hello"
        else {
            addressee = parts[0];
            sender = parts[1];
            content = parts[2];
        }

        // Build the stored message string
        String storedMessage = "From: " + sender + " | Message: " + content;

        // Store the message in the mailbox
        storeMessage(addressee, storedMessage);
    }

    // This method stores a message in the HashMap mailbox
    private static void storeMessage(String addressee, String message) {
        // If the mailbox for this addressee does not exist, create it
        if (!mailboxes.containsKey(addressee)) {
            mailboxes.put(addressee, new ArrayList<>());
        }

        // Get the list of messages for this addressee
        ArrayList<String> messages = mailboxes.get(addressee);

        // If mailbox size is limited and full
        if (mailboxSizeLimit > 0 && messages.size() >= mailboxSizeLimit) {
            // Overwrite the oldest message (remove first one)
            messages.remove(0);
        }

        // Add the new message to the mailbox
        messages.add(message);

        // Print the current mailbox contents for this user
        System.out.println("Mailbox for " + addressee + ":");
        for (String msg : messages) {
            System.out.println("  " + msg);
        }
    }
}