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

public class MultiThreadChatClient implements Runnable {

    // The client socket
    private static Socket clientSocket = null;
    // Datagram client socket
    private static MulticastSocket clientMultiSocket = null;
    // The output stream
    private static PrintStream os = null;
    // The input stream
    private static DataInputStream is = null;
    private static DatagramPacket multicastData = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    private static byte[] buffer = new byte[4096];
    private static Scanner scan = null;
    private static String name;

    public static void main(String[] args) {
        int portNumber = 2222;
        int multicastPort = 19970;
        int myId;
        scan = new Scanner(System.in);
        String host = "localhost";
        name = null;

        if (args.length < 2) {
            System.out
                    .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
                            + "Now using host=" + host + ", portNumber=" + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        try {
            clientSocket = new Socket(host, portNumber);
            // Start listening on Multicast server. Probably need a new port.
            clientMultiSocket = new MulticastSocket(multicastPort);
            InetAddress address = InetAddress.getByName("224.0.2.71");
            clientMultiSocket.joinGroup(address);
            // --
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
            System.err.println(e);
        }
        /*
         * If everything has been initialized then we want to write some data to the
         * socket we have opened a connection to on the port portNumber.
         */
        if (clientSocket != null && os != null && is != null && clientMultiSocket != null) {
            try {
                // Setup account using TCP Connection
                try {
                    boolean toRead=true;
                    while(true){
                        String line;
                        line = is.readLine();
                        if(line != null){
                            if(line.contains("Id")){
                              myId = Integer.parseInt(line.split(",")[1]);
                            }
                            if(line.contains("Hello")){
                                break;
                            }
                            if(toRead){
                                System.out.println(line);
                                name = scan.nextLine();
                                os.println(name.trim());
                                toRead = false;
                            }
                        }
                    }
                } catch (IOException e){
                    System.err.println(e);
                }
                // Once the server says Hello we're good to stop listening on the
                // TCP socket and start listening on Multicast UDP socket
                System.out.println("!!! Now listening on Multicast channel");
                /* Create a thread to read from the Multicast channel. */
                new Thread(new MultiThreadChatClient()).start();
                new Thread(new HeartBeat()).start();
                
                int msgId = 1;
                while (!closed) {
                    String inputFile =  "" + name + "-" + msgId + ".chat";
                    System.out.println("Waiting for file:" + inputFile);
                    System.out.println("Press ENTER to send");
                    scan.nextLine();
                	if(send(inputFile)){
                        msgId++;
                    }
                }
                /*
                 * Close the output stream, close the input stream, close the socket.
                 */
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    /*
     * Create a thread to read from the server. (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        /*
         * Keep on reading from the socket till we receive "Bye" from the
         * server. Once we received that then we want to break.
         */

        try {
            while(true){

                BufferedWriter output = null;
                String[] data;
                String firstIn, responseLine = "", fileName = null;

                multicastData = new DatagramPacket(buffer, buffer.length);
                clientMultiSocket.receive(multicastData);
                firstIn = new String(multicastData.getData(), multicastData.getOffset(), multicastData.getLength());

                System.out.println(firstIn);

                if(!firstIn.startsWith("***")){

                    data = firstIn.split("<");
                    boolean first = true;

                    for(String result : data){
                        if(first){
                            first = false;
                            fileName = result;
                        }else{
                            responseLine += result;
                        }
                    }
                } else {
                    responseLine = firstIn;
                }
                if (responseLine != "") {
                    try{
                        if(fileName != null){
                            output = new BufferedWriter(new FileWriter(fileName, true));
                            output.write(responseLine, 0, responseLine.length());
                            output.close();
                            output = null;
                        }
                    } catch (IOException e){
                        System.err.println(e);
                    }
                    if (responseLine.indexOf("*** Bye") != -1) {
                        closed = true;
                        break;
                    }
                    if(output != null)
                        output.close();
                }

        	}
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public static boolean send(String inputFile) {
    	String msg;
      try{
        inputLine = new BufferedReader(new FileReader(inputFile));
        while((msg = inputLine.readLine()) != null)
          os.println(msg);
      } catch (IOException e){
        System.err.println("Error Reading File");
        System.err.println(e);
        return false;
      }
      return true;
    }
}
