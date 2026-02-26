import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        Socket socket = null;             //hold the connection to the server
        PrintWriter out = null;            //used to send data to the server
        BufferedReader userInput = null;   //read input from the keyboard

        try {
            // Create a socket that connects to the server running on this computer (127.0.0.1) on port 5155
            socket = new Socket("127.0.0.1", 5155);

            // Create a PrintWriter to send data through the socket to the server
            out = new PrintWriter(socket.getOutputStream(), true);

            // Create a BufferedReader to read text typed by the user in the console
            userInput = new BufferedReader(new InputStreamReader(System.in));

            // Ask the user to type a message
            System.out.println("Enter your message (examples: Hello | 1, Hello | 1,2, Hello):");

            // Read one line of text from the user
            String message = userInput.readLine();

            // Send the message to the server
            out.println(message);

            // Tell the user the message was sent
            System.out.println("Message sent to server.");

        } catch (IOException e) {
            // Print an error message if something goes wrong
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                // Close the PrintWriter if it was opened
                if (out != null) out.close();

                // Close the socket if it was opened
                if (socket != null) socket.close();

            } catch (IOException e) {
                // Print an error message if closing fails
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}