package Socket Assignment.InClass.Part1;

import java.net.Socket;
import java.net.Sockets;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        OutputStream outStream = null;
        PrintWriter writing = null;
        Socket sock = null;

        try {
            sock = new Socket("127.0.0.1", 5155);
            outStream = sock.getOutputStream();
            writing = new PrintWriter(outStream, true);
            writing.println("Hello, server!");

        } catch (IOException ioe) {
            // Handle exception
            System.err.println(ioe);
        } finally {
            if (socket != null) {
                sock.close();
            }
        }
    }
}