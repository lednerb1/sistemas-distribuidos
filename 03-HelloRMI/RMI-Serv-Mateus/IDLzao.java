/** IDLzao.java **/
import java.rmi.*;

public interface IDLzao extends Remote {
   public int sendMsg(String name, String msg) throws RemoteException;
   public String getMsg(String name) throws RemoteException;
   public String getMsgUser(String name) throws RemoteException;
   public boolean connect(String name) throws RemoteException;
   public boolean disconnect(String name) throws RemoteException;
}
