//Example 25

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
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
    private static byte[] buffer = new byte[256];
    

    public static void main(String[] args) {

        // The default port.
        int portNumber = 2222;
        int multicastPort = 1997;
        // The default host.
        String host = "localhost";

        if (args.length < 2) {
            System.out
                    .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
                            + "Now using host=" + host + ", portNumber=" + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        /*
         * Open a socket on a given host and port. Open input and output streams.
         */
        try {
            clientSocket = new Socket(host, portNumber);
            // System.out.println("")
            // Start listening on Multicast server. Probably need a new port.
            clientMultiSocket = new MulticastSocket(multicastPort);
            InetAddress address = InetAddress.getByName("224.0.0.1");
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
        if (clientSocket != null && os != null && is != null && clientMultiSocket != null && clientMultiSocket.isConnected()) {
            try {
            	// Setup account using TCP Connection
            	try {
            		String line;
            		while(true){
            			line = is.readLine();
            			if(line != null){
            				if(line.contains("Hello "))
            					break;
            				System.out.println(line);
            				os.println(inputLine.readLine().trim());
            				
            			}
            		}
            	} catch (IOException e){
            		System.err.println(e);
            	}
            	
                /* Create a thread to read from the server. */
                new Thread(new MultiThreadChatClient()).start();
                
                while (!closed) {
                    // MAP: Efetua a leitura dzo teclado das novas mensagens do 
                    //     cliente e envia para o servidor via socket;
                    // os.println(inputLine.readLine().trim());
                	send();
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
        String responseLine;
        try {
        	while(true){
	        	multicastData = new DatagramPacket(buffer, buffer.length);
	            clientMultiSocket.receive(multicastData);
	            responseLine = multicastData.getData().toString();
	            while (responseLine != null) {
	                System.out.println(responseLine);
	                if (responseLine.indexOf("*** Bye") != -1) {
	                    break;
	                }
	            }
	            closed = true;
        	}
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
    
    public static void send() {
    	String msg;
    }
}
