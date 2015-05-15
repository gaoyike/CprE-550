/**
 * Created by chenguanghe on 2/20/15.
 */
public class ChatMessage {
    private String message;
    private String chatroom;
    private int ack = 0;
    private String username;
    private ChatMessage(){};

    public ChatMessage(String message, String chatroom, String username) {
        this.message = message;
        this.chatroom = chatroom;
        this.username = username;
    }
    public int isAck () {
        return ack;
    }
    public String getMessage(){
        return message;
    }
    public String getUsername(){
        return username;
    }
}
