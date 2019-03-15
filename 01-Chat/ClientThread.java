/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
import java.io.DataInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.net.*;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Arrays;

class ClientThread extends Thread {

  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private MulticastSocket serverMultiSocket = null;
  private InetAddress group = null;
  private int port = 0;
  private final ClientThread[] threads;
  private int maxClientsCount;
  private int myId;
  private byte[] buff = new byte[4096];

  public ClientThread(Socket clientSocket, ClientThread[] threads, MulticastSocket serverMultiSocket, InetAddress group, int portNumber, int id) {
	  this.clientSocket = clientSocket;
	  this.threads = threads;
	  this.maxClientsCount = threads.length;
    this.serverMultiSocket = serverMultiSocket;
    this.group = group;
    this.port = portNumber;
    this.myId = id+1;
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
      os.println("Hello " + name
      + " to our chat room.\nTo leave enter /quit in a new line");

      String msg = "*** A new user " + name + " entered the chat room !!! ***\n";
      DatagramPacket alert = new DatagramPacket(msg.getBytes(), msg.length(),
                                 this.group, this.port);

      serverMultiSocket.send(alert);
      BufferedWriter output;
      output = new BufferedWriter(new FileWriter(name+"-"+myId+".serv", true));

      while (true) {
        String in = is.readLine();
        output.write(in, 0, in.length());
        output.write("\n");
        output.close();
        String line = name+"-"+myId+"<";
        line += in;
        line += "\n";
        //System.out.println(line);
        if (in.startsWith("/quit")) {
          System.out.println("User " + myId + " leaving");
          break;
        }

        buff = line.getBytes();
        if(buff.length > 4096){
          System.out.println("Buffer: " + buff);
          System.out.println("Length: " + buff.length);
          DatagramPacket packet = new DatagramPacket(Arrays.copyOfRange(buff, 0, 4095), 4096,
                                                     this.group, this.port);
          serverMultiSocket.send(packet);
          int i=0;
          do{
            i++;
            packet = new DatagramPacket(Arrays.copyOfRange(buff, i*4096, Math.min(buff.length, i*4095+4096)), Math.min(buff.length, i*4095+4096) - i*4096,
                                                       this.group, this.port);
            serverMultiSocket.send(packet);
          } while(buff.length > (i*4095+4096));
        } else {
          DatagramPacket packet = new DatagramPacket(buff, line.length(),
                                                     this.group, this.port);
          serverMultiSocket.send(packet);
        }

      }
      System.out.println("connection closed");
      output.close();


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
