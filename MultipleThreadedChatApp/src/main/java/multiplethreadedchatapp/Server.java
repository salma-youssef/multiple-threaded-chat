package multiplethreadedchatapp;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class 
public class Server {
    private static final int PORT = 8726;
    // Store active clients in a vector
    private static Vector<ClientHandler> clients = new Vector<ClientHandler>();

    // count clients 
    private static int count = 0;

    public static Vector<ClientHandler> getClients() {
        return clients;
    }

    public static void main(String[] args) throws IOException {
        // creating a socket
        ServerSocket serverSocket = new ServerSocket(PORT);

        Socket socket;

        while (true) {
            // accepting requests
            socket = serverSocket.accept();

            // get input and output streams
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // client handler object for the request
            ClientHandler clientHandler = new ClientHandler(socket, inputStream, outputStream, "client" + count);

            // create a new thread with this object(for concurrency)
            Thread thread = new Thread(clientHandler);

            // add this client to active client vector
            clients.add(clientHandler);
            dumbFile("client" + count);

            // thread starting.
            thread.start();

            // a client created so increment count.
            count++;

        }

    }

    public static void dumbFile(String name) throws FileNotFoundException {

//        for (ClientHandler clientHandler : Server.clients) {
        String fileName = name + ".txt";
        PrintStream output = new PrintStream(new FileOutputStream(fileName));
//        System.setOut(output);
        output.close();
//        }


    }

} 
