import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chenguanghe on 2/20/15.
 */
public class ChatRoom implements Runnable {
    private BlockingQueue<ChatMessage> chatMessages = new LinkedBlockingQueue<ChatMessage>();
    private String roomname;
    private int port;
    private HashSet<User> users = new HashSet<User>();
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean onCreated = true;
    private ChatRoom() {
    }

    public ChatRoom(String roomname, int port) throws IOException {
        this.roomname = roomname;
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean addUser(User user) {
        if (!users.contains(user.username)) {
            users.add(user);
            return true;
        }
        return false;
    }

    public int getPort() {
        return port;
    }

    public boolean deleteUser(String username) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.username.equals(username))
            {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        if (onCreated)

        try {
            while (true) {
                socket = serverSocket.accept();
                new MutiThreadServer(socket, chatMessages, roomname, users).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
