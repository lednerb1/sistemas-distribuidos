
/** Server.java **/

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import javafx.util.Pair;
import java.util.*;

/*
private class Pair {
private String elem1;
private String elem2;

public String getUser(){
return elem1;
}

public String getMsg(){
return elem2;
}

public String[] getPair(){
String[] arr = new String[2];
arr[0] = elem1;
arr[1] = elem2;
return arr;
}

}
*/ // Old Pair

public class Server implements ServerIDL {

  class User {
    private String nome;
    private int id;

    public User(String nome, int id){
      this.nome = nome;
      this.id = id;
    }

    private Queue< Pair<String, String> > fila;

    public String getName(){
      return this.nome;
    }

    public String toString(){
      return this.nome;
    }

  }
  public static ArrayList<User> userList = new ArrayList<User>();

  public static void main(String[] args) {

    try {
      // Instancia o objeto servidor e a sua stub
      Server server = new Server();

      ServerIDL stub = (ServerIDL) UnicastRemoteObject.exportObject(server, 0);
      // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
      Registry registry = LocateRegistry.createRegistry(6600);
      //Registry registry = LocateRegistry.getRegistry(9999);
      registry.bind("getMsgUser", stub);
      registry.bind("getMsg", stub);
      registry.bind("sendMsg", stub);
      registry.bind("connect", stub);
      registry.bind("disconnect", stub);
      System.out.println("Servidor pronto");
      for(String a : registry.list()){
        System.out.println(a);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public String getMsgUser(String user) throws RemoteException{
    return null;
  }
  public String getMsg(String user) throws RemoteException{
    return null;
  }
  public void sendMsg(String user, String msg) throws RemoteException{

  }

  public boolean connect(String user) throws RemoteException{
    if(userList.size() > 10){
      return false;
    }
    for(User u : userList){
      if(u.getName().compareTo(user) == 0){
        return false;
      }
    }
    return userList.add(new User(user, 0));
  }

  public boolean disconnect(String user) throws RemoteException{
    return userList.removeIf(u -> (u.getName().compareTo(user) == 0));
  }

}
