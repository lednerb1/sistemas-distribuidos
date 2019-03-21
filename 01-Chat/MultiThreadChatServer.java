
//Example 26

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;

/*
 * A chat server that delivers public and private messages.
 */
public class MultiThreadChatServer {

  // The server socket.
  private static ServerSocket serverSocket = null;
  private static MulticastSocket serverMultiSocket = null;
  // The client socket.
  private static Socket clientSocket = null;
  private static InetAddress address = null;
  // This chat server can accept up to maxClientsCount clients' connections.
  private static final int maxClientsCount = 10;
  private static final ClientThread[] threads = new ClientThread[maxClientsCount];

  public static void main(String args[]) {

    // The default port number.
    int portNumber = 2222;
    int multicastPort = 19970;
    if (args.length < 1) {
      System.out
          .println("Usage: java MultiThreadChatServer <portNumber>\n"
              + "Now using port number=" + portNumber);
    } else {
      portNumber = Integer.valueOf(args[0]).intValue();
    }

    /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
    try {
  	  serverMultiSocket = new MulticastSocket(multicastPort);
  	  address = InetAddress.getByName("224.0.2.71");
  	  serverMultiSocket.joinGroup(address);
  	  serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        int i = 0;
        for (i = 0; i < maxClientsCount; i++) {
          if (threads[i] == null) {
            (threads[i] = new ClientThread(clientSocket, threads, serverMultiSocket, address, multicastPort, i)).start();
            break;
          }
        }
        if (i == maxClientsCount) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Server too busy. Try later.");
          os.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
