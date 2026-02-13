
import java.net.Socket;
import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        OutputStream outStream = null;
        PrintWriter writing = null;
        Socket sock = null;

        try { // 10.42.0.1
            sock = new Socket("127.0.0.1", 5155);
            outStream = sock.getOutputStream();
            writing = new PrintWriter(outStream, true);
            writing.println("Hello, server......");

        } catch (IOException ioe) {
            // Handle exception
            System.err.println(ioe);
        } finally {
            if (sock != null) {
                // sock.close();
            }
        }
    }
}