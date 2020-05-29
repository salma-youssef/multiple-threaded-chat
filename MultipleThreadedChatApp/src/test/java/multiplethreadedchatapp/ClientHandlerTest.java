package multiplethreadedchatapp;
import static org.junit.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Test;

import loginpackage.Login;

public class ClientHandlerTest {

	//@org.junit.Test
    public void setTokenSuccessfully() throws FileNotFoundException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        ClientHandler clientHandler = new ClientHandler(null, null, null, "client0");
        clientHandler.setToken(map, "salma", 0);
        int occurrences = map.get("salma");
        assertEquals(1, occurrences);
    }

	//@org.junit.Test
    public void incrementWordInToken() throws FileNotFoundException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("salma", 2);
        ClientHandler clientHandler = new ClientHandler(null, null, null, "client0");
        clientHandler.setToken(map, "salma", 0);
        int occurrences = map.get("salma");
        assertEquals(3, occurrences);
    }

	@Test
    public void incrementManyOccurrences() throws FileNotFoundException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("salma", 4);
        ClientHandler clientHandler = new ClientHandler(null, null, null, "client0");
        clientHandler.setToken(map, "salma", 2);
        int occurrences = map.get("salma");
        assertEquals(6, occurrences);
    
    }


	
	/*@Test
	// failed, cause expected "salma:4"
	public void printMapSuccess1() throws FileNotFoundException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("salma", 4);
        ClientHandler clientHandler = new ClientHandler(null, null, null, "client0");
        clientHandler.printMapToFile(map, "client0");
        String fileName1 = "client0" + "WordOccurrences" + ".txt";
        Scanner scanner = new Scanner(new File(fileName1));
        while(scanner.hasNextLine()) {
        	String line = scanner.nextLine();
        	assertEquals("salma:2", line); 
        }
        
	}*/
	@Test
	//succeed
	public void printMapSuccess2() throws FileNotFoundException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("salma", 4);
        ClientHandler clientHandler = new ClientHandler(null, null, null, "client0");
        clientHandler.printMapToFile(map, "client0");
        String fileName1 = "client0" + "WordOccurrences" + ".txt";
        Scanner scanner = new Scanner(new File(fileName1));
        while(scanner.hasNextLine()) {
        	String line = scanner.nextLine();
        	assertEquals("salma:4", line); 
        }
        
	}
	
	
}