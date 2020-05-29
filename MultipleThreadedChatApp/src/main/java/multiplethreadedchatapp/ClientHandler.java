package multiplethreadedchatapp;

import loginpackage.Login;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private final DataInputStream INPUTSTREAM;
    private final DataOutputStream OUTPUTSTREAM;
    private static Vector<String> ignoredClients = new Vector<String>();
    private String name;
    private static Scanner scanner = new Scanner(System.in);


    public ClientHandler(Socket socket, DataInputStream inputStream, DataOutputStream outputStream, String name) {
        this.socket = socket;
        this.INPUTSTREAM = inputStream;
        this.OUTPUTSTREAM = outputStream;
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void run() {

        try {
            boolean isValidUser = handleLogin();
//                OUTPUTSTREAM.writeUTF("Invalid Credentials");
            if (isValidUser) {
                String typed;
                OUTPUTSTREAM.writeUTF("Welcome to our group chat...");
                while (true) {
                    try {
                        // read the typed string

                        typed = INPUTSTREAM.readUTF();
//                        System.out.println(typed);
                        // if client typed Bye Bye close connection
                        if (typed.toLowerCase().contains("bye")) {
                            dumpOccurrencesOfWords();
                            dumpAllOccurrences();
                            System.out.println("Name ==> " + this.name);
                            ignoredClients.add(this.name);
                            OUTPUTSTREAM.writeUTF("Quitting, Thank you.");
//                            break;
                        } else if (typed.contains("#")) {
                            // break the string into message and sender part
                            StringTokenizer st = new StringTokenizer(typed, "#");
                            String firstToken = st.nextToken();
                            String secondToken = st.nextToken();

                            sendMessageToAll(typed, secondToken);
                        } else {
                            System.out.println("Please write the message in the correct format");
                        }


                    } catch (IOException e) {
//                        e.printStackTrace();
                        System.out.println("Stream is broken");
                        break;
                    }

                }

                // closing resources
//                this.INPUTSTREAM.close();
//                this.OUTPUTSTREAM.close();
//                this.socket.close();
            }
        } catch (IOException e) {
            System.out.println("Second Exception");
        }
    }


    public void dumpOccurrencesOfWords() throws IOException {
        for (ClientHandler clientHandler : Server.getClients()) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            String fileName = clientHandler.name + ".txt";
            try {
                Scanner txtFile = new Scanner(new File(fileName));
                while (txtFile.hasNextLine()) {
                    String line = txtFile.nextLine();
                    StringTokenizer stringTokenizer = null;
                    if (!line.isEmpty()) {
                        if (line.contains("#")) {
                            stringTokenizer = new StringTokenizer(line, "#");
                            String token = stringTokenizer.nextToken();
                            String sender = stringTokenizer.nextToken();
                            if (sender.equals(clientHandler.name)) {
                                if (token.contains(" ")) {
                                    stringTokenizer = new StringTokenizer(token, " ");
                                    while (stringTokenizer.hasMoreTokens()) {
                                        String word = stringTokenizer.nextToken();
                                        setToken(map, word, 0);
                                    }
                                } else {
                                    setToken(map, token, 0);
                                }
                            }
                        }
                    } else {
                        System.out.println("Empty File");
                    }
                }
                printMapToFile(map, clientHandler.getName());
                txtFile.close();
            } catch (IOException e) {
                System.out.println(fileName + " not found.");
            }
        }


    }

    public void sendMessageToAll(String typed, String secondToken) throws IOException {
        // search for the recipient in the connected clients vector. 
        for (ClientHandler clientHandler : Server.getClients()) {
            if (!clientHandler.name.equals(secondToken)) {
                clientHandler.OUTPUTSTREAM.writeUTF(typed);
            }

            if (!ignoredClients.contains(clientHandler.name)) {
                FileWriter fileWriter = new FileWriter(clientHandler.getName() + ".txt", true);
                fileWriter.write(typed + "\n");
                fileWriter.close();
            }
        }
    }

    public boolean handleLogin() {
        try {
//            boolean quit = false;
            String input;
            while (true) {
                OUTPUTSTREAM.writeUTF("Already have an account? (Y -> to sign in, N -> to register, Q -> to quit)");
                input = INPUTSTREAM.readUTF().toLowerCase();
                if (input.equals("q")) {
//                    quit = true;
                    OUTPUTSTREAM.writeUTF("Bye...");
                    break;
                } else if (input.equals("y")) {
                    OUTPUTSTREAM.writeUTF("Enter username: ");
                    String username = INPUTSTREAM.readUTF();
                    OUTPUTSTREAM.writeUTF("Enter password: ");
                    String password = INPUTSTREAM.readUTF();
                    boolean isValid = Login.authenticate(username, password);
                    if (isValid) {
                        OUTPUTSTREAM.writeUTF("You are now logged in...");
                        return true;
                    } else {
                        OUTPUTSTREAM.writeUTF("Invalid credentials...");
                    }
                } else if (input.equals("n")) {
                    OUTPUTSTREAM.writeUTF("Enter new username: ");
                    String username = INPUTSTREAM.readUTF();
                    OUTPUTSTREAM.writeUTF("Enter new password: ");
                    String password = INPUTSTREAM.readUTF();
                    boolean isRegistered = Login.register(username, password);
                    if (isRegistered) {
                        OUTPUTSTREAM.writeUTF("Registeration is successfull");
                    } else {
                        OUTPUTSTREAM.writeUTF("Username already exists");
                    }
                } else {
                    OUTPUTSTREAM.writeUTF("Invalid input.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setToken(HashMap<String, Integer> map, String token, int occurrences) {
        if (map.containsKey(token)) {
            int count = (occurrences == 0) ? map.get(token) + 1 : map.get(token) + occurrences;
//            int oldValue = map.get(token);
//            map.remove(token);
//            map.put(token, count);
            map.replace(token, count);
        } else {
            if (occurrences == 0)
                map.put(token, 1);
            else
                map.put(token, occurrences);
        }
    }

    public void printMapToFile(HashMap<String, Integer> map, String fileName) {
        String fileName1 = fileName + "WordOccurrences" + ".txt";
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            System.out.println("=====================");
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
        try {
            FileWriter fileWriter = new FileWriter(fileName1, false);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                fileWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dumpAllOccurrences() throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (ClientHandler clientHandler : Server.getClients()) {
            String fileName = clientHandler.getName() + "WordOccurrences.txt";
            Scanner txtFile = new Scanner(new File(fileName));
            while (txtFile.hasNextLine()) {
                String line = txtFile.nextLine();
                if (!line.isEmpty() && line.contains(":")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ":");
                    String key = stringTokenizer.nextToken();
                    String value = stringTokenizer.nextToken();
                    setToken(map, key, Integer.parseInt(value));
                }
            }
        }
        printMapToFile(map, "final");
    }
}