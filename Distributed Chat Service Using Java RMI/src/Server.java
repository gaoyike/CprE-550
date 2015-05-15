import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenguanghe on 2/20/15.
 */
public class Server extends UnicastRemoteObject implements Message {
    ConcurrentHashMap<String,ChatRoom> chatRooms = new ConcurrentHashMap<String, ChatRoom>();
    ConcurrentHashMap<String, Integer> charRoomPort = new ConcurrentHashMap<String, Integer>();
    public Server() throws RemoteException
    {
    }
    @Override
    public boolean CreateChatRoom(String name) throws RemoteException {
        Random random = new Random();
        int port = random.nextInt(30000);
        while (charRoomPort.containsValue(port) || port< 10000)
            port = random.nextInt(30000);
        if (chatRooms.containsKey(name))
            return false;
        else {
            ChatRoom newRoom = null;
            try {
                newRoom = new ChatRoom(name, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            chatRooms.put(name, newRoom);
            charRoomPort.put(name, port);
            new Thread(newRoom).start();
            return true;
        }
    }

    @Override
    public boolean DeleteChatRoom(String name) throws RemoteException {
        if (!chatRooms.containsKey(name))
            return false;
        else {
            chatRooms.remove(name);
            charRoomPort.remove(name);
            return true;
        }
    }

    @Override
    public boolean JoinChatRoom(String chatroom, String username, String hostname) throws RemoteException {
        if (!chatRooms.containsKey(chatroom))
            return false;
        else {
            ChatRoom chatRoom = chatRooms.get(chatroom);
            if (chatRoom.addUser(new User(username,hostname,chatroom)))
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean LeaveChatRoom(String chatroom,  String username) throws RemoteException {
        if (!chatRooms.containsKey(chatroom))
            return false;
        else {
            ChatRoom chatRoom = chatRooms.get(chatroom);
            if (!chatRoom.isEmpty()) {
                chatRoom.deleteUser(username);
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public int getPortname(String chatroom) throws RemoteException {
        return charRoomPort.get(chatroom);
    }

    @Override
    public String[] ChatRoomList() throws RemoteException {
        Set<Map.Entry<String, Integer>> set = charRoomPort.entrySet();
        String [] str = new String[set.size()];
        Iterator<Map.Entry<String, Integer>> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry e = iterator.next();
            str[i] = e.getKey()+"|"+e.getValue();
            i++;
        }
        return str;
    }
}
