/** HelloWorld.java **/
import java.rmi.*;

public interface ServerIDL extends Remote {
   public String getMsgUser(String user) throws RemoteException;
   public String getMsg(String user) throws RemoteException;
   public void sendMsg(String user, String msg) throws RemoteException;
   public boolean connect (String user) throws RemoteException;
   public boolean disconnect (String user) throws RemoteException;
   public boolean removeLastMsg(String user) throws RemoteException;
}
