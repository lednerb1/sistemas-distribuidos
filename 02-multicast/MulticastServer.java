import java.io.*;
import java.net.*;

public class MulticastServer {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		DatagramSocket inSocket = null;
		DatagramSocket outSocket = null;
		BufferedReader fileInput = null;
		byte[] buffer;
		final int PORT = 19970;
		String name = scan.nextLine();

		try {
			socket = new DatagramSocket();
			long counter = 0;
			Sring msg = name;
			fileInput = new BufferedReader(new FileReader(name+""));

			while(true){
				msg = msg + fileInput.readLine();
				
			}
		}
	}
}
