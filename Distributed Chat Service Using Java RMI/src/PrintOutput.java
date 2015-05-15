import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenguanghe on 2/21/15.
 */
public class PrintOutput extends Thread{
    private PrintOutput(){};
    private BlockingQueue<ChatMessage> chatMessages;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean isRunning = true;
    private boolean ack = false;
    public PrintOutput( BlockingQueue<ChatMessage> chatMessages,PrintWriter out) {
        this.chatMessages = chatMessages;
        this.out = out;
        this.in = in;
    }
    public void setAck(boolean ack) {
        this.ack = ack;
    }
    public synchronized void run(){
        ChatMessage output;
        try {
            while (isRunning) {
                output = chatMessages.take();
                if (output.equals("!q")){
                    isRunning = false;
                    out.close();
                }
                out.println(output.getUsername()+":"+output.getMessage());
                while (!ack){
                    //wait for ack
                }
                System.out.println("Acked: " + output.getMessage());
                ack = false;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }
    }
}
