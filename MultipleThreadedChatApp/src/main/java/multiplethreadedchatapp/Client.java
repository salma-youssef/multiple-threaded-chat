package multiplethreadedchatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private final static int PORT = 8726;


    public static void main(String[] args) throws UnknownHostException, IOException {

        final Scanner SCANNER = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("localhost");

        // establish a connection 
        final Socket socket = new Socket(ip, PORT);


        final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        // send message thread
        Thread sendThread = new Thread(new Runnable() {

            public void run() {
                boolean quit = false;
                while (true) {


                    String message = SCANNER.nextLine();

                    try {
                        // write message on the output stream
                        if (!quit) {
                            if (message.contains("bye")) {
                                quit = true;
                            }
                            outputStream.writeUTF(message);
                        }
                    } catch (IOException e) {;
                        System.out.println("You can no longer send messages");
                        break;
                    }
                }
            }
        });

        // read thread 
        Thread readThread = new Thread(new Runnable() {

            public void run() {

                boolean quit = false;
                while (true) {
                    try {
                        // read message coming on the input stream to the client
                        if (!quit) {
                            String message = inputStream.readUTF();
                            System.out.println(message);
                            if (message.contains("Quitting")) {
                                quit = true;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("You can no longer read messages");
                        break;
                    }
                }
            }
        });

        sendThread.start();
        readThread.start();

    }
} 