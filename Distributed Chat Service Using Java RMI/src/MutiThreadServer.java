import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenguanghe on 2/21/15.
 */
public class MutiThreadServer extends Thread{
    private Socket socket;
    private BlockingQueue<ChatMessage> chatMessages;
    private String username;
    private String chatroom;
    private volatile boolean isRunning = true;
    private HashSet<User> users;
    public MutiThreadServer(Socket socket, BlockingQueue<ChatMessage> chatMessages, String chatroom, HashSet<User> users){
        super("start");
        this.socket = socket;
        this.chatMessages = chatMessages;
        this.chatroom = chatroom;
        this.users = users;
    }
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            username = in.readLine();
            out.println("Server: Welcome! "+username);
            PrintOutput print = new PrintOutput(chatMessages,out);
            print.start();
            String input;
            while (isRunning){
                input = in.readLine();
                System.out.println("input: "+input+" num of Users: " + users.size());
                if (input.contains("|ack")){
                    print.setAck(true);
                    continue;
                }
                for (int i = 0 ; i < users.size(); i++)
                {
                    chatMessages.add(new ChatMessage(input, chatroom, username));
                }
                if (input.equals("!q")){
                    System.out.println("Close Connection with "+username+" in "+chatroom);
                    isRunning = false;
                    print.interrupt();
                }
            }
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
        }
    }
}
