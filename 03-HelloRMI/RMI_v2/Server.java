
/** Server.java **/

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import javafx.util.Pair;
import java.util.*;
import java.io.IOException;
import java.util.LinkedList;

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
    private String nome, diretorio, arquivo;
    private int id, contadorMsg;
    private GerenciadorArquivo ga;

    public User(String nome, int id) throws IOException{
      this.nome = nome;
      this.id = id;
      this.contadorMsg=0;//para renomearArquivo

      fila = new LinkedList<>();
      ga = new GerenciadorArquivo(nome,".serv");

    }
    //User, message
    private Queue< Pair<String, String> > fila;

    public String getName(){
      return this.nome;
    }

    public String toString(){
      return this.nome;
    }

    public boolean addFila(String User, String Msg){
      //nao adicionar a mensagem ao proprio dono
      if(this.getName().compareTo(User)==0)
        return true;

      //GerenciadorArquivo adicionar no arquivo

      contadorMsg++;
      // ga.renomearArquivo(ga.getDiretorio()+ga.getName()+".serv"); //definir forma para renomear arquivo a cada mensagem conforme o contadorMsg
      return fila.add(new Pair(User,Msg));
    }

    //remover ultima msg
    public boolean removerFila(){
      Pair p = fila.poll();
      if(p!=null)
      return true;

      return false;
    }

    public String getUserFila(){
      Pair p = fila.peek();
      if(p==null)
        return null;

      return (String) p.getKey();
    }

    public String getMsgFila(){
      Pair p = fila.peek();
      if(p==null)
        return "";

      return (String) p.getValue();
    }


  }

  private boolean getMsg=false, sendMsg=false;

  public static ArrayList<User> userList = new ArrayList<User>();

  public static void main(String[] args) {

    try {
      // Instancia o objeto servidor e a sua stub
      Server server = new Server();

      ServerIDL stub = (ServerIDL) UnicastRemoteObject.exportObject(server, 0);
      // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
      Registry registry = LocateRegistry.createRegistry(6600);
      //Registry registry = LocateRegistry.getRegistry(9999);
      // registry.bind("getMsgUser", stub);
      // registry.bind("getMsg", stub);
      // registry.bind("sendMsg", stub);
      // registry.bind("connect", stub);
      // registry.bind("disconnect", stub);
      registry.bind("Chat", stub);
      System.out.println("Servidor pronto");
      // for(String a : registry.list()){
      //   System.out.println(a);
      // }



    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public String getMsgUser(String user) throws RemoteException{
    //pega usuario da fila de tuplas

    String usuario = null;
    for(User u : userList){
      if(u.getName().compareTo(user)==0)
      usuario = u.getUserFila();
    }

    return usuario;
  }

  public boolean removeLastMsg(String user){
    for(User u : userList){
      if(u.getName().compareTo(user)==0)
      return u.removerFila();
    }
    return false;
  }

//ideia 1: contador, quando getMsg/getMsgUser for chamado incrementa, qnd os dois forem chamados, excluir mensagem
  public String getMsg(String user) throws RemoteException{
    //pega mensagem da fila de tuplas

    for(User u : userList){

      if(u.getName().compareTo(user)==0){
        return u.getMsgFila();
      }
        // return u.getMsgFila(user);
    }
    return null;
  }

  public void sendMsg(String user, String msg) throws RemoteException{
    //escrever mensagem no arquivo e nas filas

    //percorrer os usuarios e adicionar a mensagem. Excluir o dono da mensagem é funcao do addFila, pois essa adiciona no arquivo tbm...
    for(User u : userList){
      u.addFila(user, msg);
      try{
      if(user.compareTo("System")==0)//nao escrever  mensagens de log do sistema
      continue;
      else if(u.getName().compareTo(user)==0)
      u.ga.escrever(msg+"\n");

      System.out.println(user+": "+msg);//printando no servidor
    }catch(IOException e){
      e.printStackTrace();
    }
    }

  }

//definir um metodo para escolher user id unico mesmo com nomes iguais....
  public boolean connect(String user) throws RemoteException{
    if(userList.size() > 10){
      return false;
    }

    for(User u : userList){
      if(u.getName().compareTo(user) == 0){
        return false;
      }
    }

    boolean resposta = false;

    try{
      resposta = userList.add(new User(user, 0));
    }catch(IOException e){
      e.printStackTrace();
    }
    if(resposta == true)
    sendMsg("System", "Cliente: "+user+" entrou");
    System.out.println("Cliente: "+user+" entrou");
    return resposta;
  }

  public boolean disconnect(String user) throws RemoteException{
    if(userList.removeIf(u -> (u.getName().compareTo(user) == 0))){
      sendMsg("System", "Cliente: "+user+" saiu");
      System.out.println("Cliente: "+user+" saiu");
      return true;
    }

    return false;
  }

}
