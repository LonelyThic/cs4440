import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            // THREAD 1 → RECEIVE DATA
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;

                    while ((message = in.readLine()) != null) {

                        System.out.println("Received: " + message);

                        // store message in mailbox
                        Server.mailbox.put(message);
                    }

                } catch (Exception e) {
                    System.out.println("Client disconnected.");
                }
            });

            // THREAD 2 → SEND DATA
            Thread sendThread = new Thread(() -> {
                try {
                    while (true) {

                        String msg =
                                Server.mailbox.take();

                        out.println("Server Echo: " + msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();
            sendThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}