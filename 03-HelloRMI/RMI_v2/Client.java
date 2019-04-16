/** HelloClient.java **/
import java.rmi.registry.*;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.List;

import java.io.IOException;


//teste simulando cliente
public class Client extends Thread{

    public static String nomeUsuario = null;
    public static ServerIDL stub = null;
    public static int nroLinha=0, max=0;
    public List<String> list = null;

    public String ler() throws IOException{//le uma linha

      if(list ==null){
        list = Files.readAllLines(Paths.get(nomeUsuario+".txt"));
        max = list.size();
      }
      nroLinha +=1;

      if(nroLinha-1 < list.size())
      return list.get(nroLinha-1);

      return null;
    }

    @Override
    public void run(){//thread secundaria

      while(true){

          if(nomeUsuario==null)
            continue;
            try{

              String msg = ler();
              if(msg!= null){
                stub.sendMsg(nomeUsuario, msg);
                System.out.println(nomeUsuario+": "+msg);//usuario deve printar suas mensagens
              }else{
                break;
              }
        }catch(Exception e){
          e.printStackTrace();
        }
      }

    }

   public static void main(String[] args){
      String host = (args.length < 1) ? null : args[0];

      if(args[1]==null )//ter nome
      System.exit(-1);

      nomeUsuario = args[1];


      try {

         // Obtém uma referência para o registro do RMI
         Registry registry = LocateRegistry.getRegistry(host,6600);

         stub =  (ServerIDL) registry.lookup("Chat");

         stub.connect(nomeUsuario);

         new Client().start();
         // while(max>= nroLinha){//fim do arquivo de envio parar?
         while(true){//nunnca parar?
           String msg= stub.getMsg(nomeUsuario);
           String user = stub.getMsgUser(nomeUsuario);
           if(msg==null||user==null){
             // System.out.println("Null: msg?:+"+msg==null+" user?: "+(user==null));
             // System.out.println("M");
             continue;
          }

           if(stub.removeLastMsg(nomeUsuario)){
             System.out.println("Msg rcv| "+user+": "+ msg);
           }
           else{
             System.out.println("Um erro ocorreu ao receber a mensagem");
           }

         }

         // stub.disconnect(nomeUsuario);
         // System.exit(0);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

   }

}
