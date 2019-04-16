
/** Servidorzinho.java **/

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.file.Paths;


class Parzinho{
  public String user;
  public String msg;

  public Parzinho (String usuario, String message){
    user = usuario;
    msg = message;
  }
}

class Filinha{
  public String owner;
  public CopyOnWriteArrayList<Parzinho> queue;
  public Filinha (String nome){
    owner = nome;
    queue = new CopyOnWriteArrayList<Parzinho>();
  }
}

public class Servidorzinho implements IDLzao {
  private static int maxUsers = 10;
  private static CopyOnWriteArrayList<Filinha> usersList;

  public Servidorzinho() {}

    public static void main(String[] args) {
      try {
        // Instancia o objeto servidor e a sua stub
        Servidorzinho server = new Servidorzinho();
        IDLzao stub = (IDLzao) UnicastRemoteObject.exportObject(server, 0);
        // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
        Registry registry = LocateRegistry.createRegistry(6600);
        //Registry registry = LocateRegistry.getRegistry(9999);
        usersList = new CopyOnWriteArrayList<Filinha>();
        registry.bind("sendMsg", stub);
        registry.bind("getMsg", stub);
        registry.bind("getMsgUser", stub);
        registry.bind("disconnect", stub);
        registry.bind("connect", stub);
        System.out.println("Servidor pronto");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    public int sendMsg(String name, String msg) throws RemoteException {
      System.out.println(name + " enviou uma mensagem");
      for (Filinha  a : usersList) {
        if (!a.owner.equals(name)) {
          Parzinho parpar = new Parzinho(name,msg);
          a.queue.add(parpar);
          int foi = salvar(a.owner,name,msg);
        }
      }
      return 0;
    }

    public String getMsg(String name) throws RemoteException {
      System.out.println(name + " pediu uma mensagem da sua fila");
      for (Filinha  a : usersList) {
        if (a.owner.equals(name)) {
          if (!a.queue.isEmpty()) {
            String mensagem = a.queue.get(0).msg;
            a.queue.remove(0);
            return mensagem;
          }
        }
      }
      return "";
    }

    public String getMsgUser(String name) throws RemoteException {
      System.out.println(name + " pediu o usuario da msg da fila propria");
      for (Filinha  a : usersList) {
        if (a.owner.equals(name)) {
          if (!a.queue.isEmpty()) {
            return a.queue.get(0).user;
          }
        }
      }
      return "";
    }

    public boolean connect(String name) throws RemoteException {
      System.out.println(name + " tentando se conectar");
      for (Filinha  a : usersList) {
        if (a.owner.equals(name)) {
          System.out.println(name + " usuario ja utilizado");
          return false;
        }
      }
      if (usersList.size() >= maxUsers) {
        System.out.println(name + " servidor cheio");
        return false;
      }
      Filinha fila = new Filinha(name);
      usersList.add(fila);
      System.out.println(name + " se conectou");
      return true;
    }

    public boolean disconnect(String name) throws RemoteException {
      for (Filinha  a : usersList) {
        if (a.owner.equals(name)) {
          usersList.remove(a);
          System.out.println("vlw, flw " + name);
          return true;
        }
      }
      System.out.println("O usuario " + name + " nao existe");
      return false;
    }

    public int salvar(String pastauser, String name, String msg) throws RemoteException {
      byte[] inBuf = new byte[5000000];
      String resLinha = "";
      String filename;
      int counter = 1;

      try{
        if (!Paths.get(pastauser).toFile().exists()) {
          File dir = new File(pastauser+"/");
          dir.mkdirs();
        }
        while(true) {
          if (counter > 9) {
            filename = pastauser+"/"+name+"-"+counter+".serv";
          } else {
            filename = pastauser+"/"+name+"-0"+counter+".serv";
          }
          if (Paths.get(filename).toFile().exists()) {
            counter++;
            continue;
          } else {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.close();
            break;
          }
        }
      } catch (IOException e) {
        System.out.println("IOException:  " + e);
      }
      return 0;
    }

  }
