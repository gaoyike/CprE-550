import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by chenguanghe on 2/20/15.
 */
public interface Message extends Remote {
    public boolean CreateChatRoom(String name) throws RemoteException;
    public boolean DeleteChatRoom(String name) throws RemoteException;
    public boolean JoinChatRoom(String chatroom, String username, String hostname) throws RemoteException;
    public boolean LeaveChatRoom(String chatroom,  String username) throws RemoteException;
    public int getPortname(String chatroom) throws RemoteException;
    public String[] ChatRoomList() throws RemoteException;
}
