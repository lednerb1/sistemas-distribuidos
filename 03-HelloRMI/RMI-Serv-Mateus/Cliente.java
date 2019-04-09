/** Cliente.java **/
import java.rmi.registry.*;
import java.io.*;

public class Cliente {
  public static void main(String[] args) {
    String host = (args.length < 1) ? null : args[0];
    BufferedReader inputLine = null;
    String user;
    int eai;
    boolean foi;
    try {
      // Obtém uma referência para o registro do RMI
      Registry registry = LocateRegistry.getRegistry(host,6600);

      // Obtém a stub do servidor
      IDLzao stub= (IDLzao) registry.lookup("connect");
      IDLzao stub2= (IDLzao) registry.lookup("disconnect");
      IDLzao stub3= (IDLzao) registry.lookup("sendMsg");
      IDLzao stub4= (IDLzao) registry.lookup("getMsg");
      IDLzao stub5= (IDLzao) registry.lookup("getMsgUser");

      inputLine = new BufferedReader(new InputStreamReader(System.in));
      // Chama o método do servidor e imprime a mensagem
      while (true) {
        System.out.println("Digite um nome de usuario valido");
        user = inputLine.readLine();
        foi = stub.connect(user);
        if (foi) break;
      }

      while(true) {
        System.out.println("Ola, o que deseja escrever?");
        String msg = inputLine.readLine();

        if (msg.startsWith("/quit")) {
          break;
        } else if (msg.startsWith("/receber")){

        }

        eai = stub3.sendMsg(user, msg);
        if (eai == 0) System.out.println("Mensagem enviada com sucesso");

        String usuario = stub5.getMsgUser(user);
        System.out.println(usuario);

        msg = stub4.getMsg(user);
        System.out.println(msg);

        if (user == "") break;
      }
      foi = stub2.disconnect(user);
      if (foi) System.out.println(user + " desconectado");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
