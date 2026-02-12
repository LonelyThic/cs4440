package Part2;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Mailbox Server – stores messages for different users
 * Supports three message formats:
 *   "Hello"                 → store with no sender, no specific addressee
 *   "1, Hello"              → for user "1", no sender known
 *   "2,1, Welcome back"     → from user "2" to user "1"
 *
 * Uses HashMap<String, List<String>> to store messages per addressee
 * Supports limited mailbox size (set to 0 for unlimited)
 */
public class Server {
    // Mailbox storage: addressee → list of messages
    private static final Map<String, List<String>> mailbox = new HashMap<>();

    // Set to 0 for unlimited, or positive number to limit total messages across all users
    private static final int MAX_TOTAL_MESSAGES = 0;  // Change to e.g. 20 to test limit

    // Counter for total messages (used only when limit > 0)
    private static int totalMessagesStored = 0;

    public static void main(String[] args) {
        int port = 5155;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mailbox Server started on port " + port);
            System.out.println("Mailbox size limit: " + (MAX_TOTAL_MESSAGES == 0 ? "unlimited" : MAX_TOTAL_MESSAGES));

            // Run forever – accept many clients one after another
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle this client in a simple sequential way (one at a time)
                handleClient(clientSocket);

                clientSocket.close();
                System.out.println("Client disconnected. Waiting for next connection...");
            }

        } catch (IOException e) {
            System.err.println("Server crashed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles one client connection: reads one message, processes it, stores it
     */
    private static void handleClient(Socket client) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );

            String message = reader.readLine();
            if (message == null || message.trim().isEmpty()) {
                System.out.println("Received empty message → ignoring");
                return;
            }

            System.out.println("Received: " + message);

            // Parse and store the message
            storeMessage(message.trim());

            // Optional: send simple acknowledgment back
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
            writer.println("Message received and stored.");

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    /**
     * Parses message and stores it in the mailbox
     * @param msg raw message string from client
     */
    private static void storeMessage(String msg) {
        String[] parts = msg.split(",", 3);  // max 3 parts: [from], [to], [text] or fewer
        String from = "unknown";
        String to = "general";               // default if no addressee
        String text;

        if (parts.length == 1) {
            // "Hello"
            text = parts[0].trim();
        } else if (parts.length == 2) {
            // "1, Hello"  → to = "1"
            to = parts[0].trim();
            text = parts[1].trim();
        } else if (parts.length >= 3) {
            // "2,1, Welcome" → from = "2", to = "1"
            from = parts[0].trim();
            to = parts[1].trim();
            text = parts[2].trim();
        } else {
            System.out.println("Invalid format → ignoring");
            return;
        }

        // Build final stored message format
        String storedMsg = String.format("[%s → %s] %s", from, to, text);

        // Check mailbox limit
        if (MAX_TOTAL_MESSAGES > 0 && totalMessagesStored >= MAX_TOTAL_MESSAGES) {
            System.out.println("Mailbox full (" + MAX_TOTAL_MESSAGES + ") → rejecting: " + storedMsg);
            return;
        }

        // Store message
        mailbox.computeIfAbsent(to, k -> new ArrayList<>()).add(storedMsg);
        totalMessagesStored++;

        // Show current mailbox status
        printMailbox();
    }

    /**
     * Prints current content of all mailboxes
     */
    private static void printMailbox() {
        System.out.println("\n=== Current Mailbox ===");
        if (mailbox.isEmpty()) {
            System.out.println("(empty)");
        } else {
            for (Map.Entry<String, List<String>> entry : mailbox.entrySet()) {
                System.out.println("Mailbox for " + entry.getKey() + ":");
                for (String m : entry.getValue()) {
                    System.out.println("  " + m);
                }
            }
        }
        System.out.println("Total messages stored: " + totalMessagesStored + "\n");
    }
}