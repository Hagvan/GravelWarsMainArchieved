package gravelwars.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TFTcpServer {

    public TFTcpServer() {

    }

    public void Start() {
        try {
            ServerSocket socket = new ServerSocket(8000);
            while (!socket.isClosed()) {
                Socket client = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                while (client.isConnected()) {
                    String received = reader.readLine();
                    System.out.println(received);
                    writer.println(received);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
