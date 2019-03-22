//Example 25

import java.io.DataInputStream;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;

public class HeartBeat implements Runnable {

    private Socket clientSocket = null;
    private DataInputStream is = null;
    private PrintStream os = null;

    public HeartBeat(Socket clientSocket) {
	try{
       		this.clientSocket = clientSocket;
        	this.is = new DataInputStream(this.clientSocket.getInputStream());
        	this.os = new PrintStream(this.clientSocket.getOutputStream());
	} catch (Exception e){
		
	}
    }

    public void run() {
	try{
        	while(true){
            		String beat = is.readLine();
            		if(beat.equals("ping"))
               			os.println("pong");
       		 }
	} catch(IOException e){
	
	}    
    }
}
