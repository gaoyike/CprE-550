/**
 * Created by chenguanghe on 2/21/15.
 */
public class User {
    public String username;
    public String userhost;
    public String currentChatRoom;
    private User(){}

    public User(String username, String userhost, String currentChatRoom){
        this.userhost = userhost;
        this.username = username;
        this.currentChatRoom = currentChatRoom;
    }

}
