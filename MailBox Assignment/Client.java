import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        try {
            Socket socket =
                    new Socket("localhost", 5000);

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);

            BufferedReader keyboard =
                    new BufferedReader(
                            new InputStreamReader(System.in));

            // THREAD 1 → RECEIVE FROM SERVER
            Thread receiveThread = new Thread(() -> {
                try {
                    String response;

                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // THREAD 2 → SEND TO SERVER
            Thread sendThread = new Thread(() -> {
                try {
                    String message;

                    while ((message = keyboard.readLine()) != null) {
                        out.println(message);
                    }

                } catch (IOException e) {
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