/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.*;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.ServerSocket;

class ClientThread extends Thread {

  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private MulticastSocket serverMultiSocket = null;
  private InetAddress group = null;
  private int port = 0;
  private final ClientThread[] threads;
  private int maxClientsCount;

  public ClientThread(Socket clientSocket, ClientThread[] threads, MulticastSocket serverMultiSocket, InetAddress group, int portNumber) {
	  this.clientSocket = clientSocket;
	  this.threads = threads;
	  this.maxClientsCount = threads.length;
    this.serverMultiSocket = serverMultiSocket;
    this.group = group;
    this.port = portNumber;
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    ClientThread[] threads = this.threads;
    MulticastSocket serverMultiSocket = this.serverMultiSocket;

    try {
      /*
       * Create input and output streams for this client.
       */
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      os.println("Enter your name.");
      String name = is.readLine().trim();
      System.out.println("...");
      os.println("Hello " + name
      + " to our chat room.\nTo leave enter /quit in a new line");

      String msg = "*** A new user " + name + " entered the chat room !!! ***\n";
      DatagramPacket alert = new DatagramPacket(msg.getBytes(), msg.length(),
                                 this.group, this.port);
      serverMultiSocket.send(alert);

      /* This notified everyone that this client joined
      for (int i = 0; i < maxClientsCount; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].os.println("*** A new user " + name
              + " entered the chat room !!! ***");
        }
      }
      */
      while (true) {
        String line = is.readLine();
        if (line.startsWith("/quit")) {
          break;
        }

        DatagramPacket packet = new DatagramPacket(line.getBytes(), line.length(),
                                    this.group, this.port);

        serverMultiSocket.send(packet);
        /* This used to send the same data to all clients one by one;
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null) {
            threads[i].os.println("<" + name + "&gr; " + line);
          }
        }
        */

      }
      for (int i = 0; i < maxClientsCount; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].os.println("*** The user " + name
              + " is leaving the chat room !!! ***");
        }
      }
      os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
      for (int i = 0; i < maxClientsCount; i++) {
        if (threads[i] == this) {
          threads[i] = null;
        }
      }

      /*
       * Close the output stream, close the input stream, close the socket.
       */
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {

    }
  }
}
