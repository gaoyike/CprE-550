import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by chenguanghe on 2/21/15.
 */
public class PrintInput extends Thread{
    private PrintInput(){};
    private  BufferedReader in;
    private PrintWriter out;
    public PrintInput(BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
    }
    public void run(){
        String input;
        try {
            while ((input=in.readLine()) != null){
                System.out.println(input);
                out.println(input + "|ack");
                System.out.println("--------------------System Info: ack->"+input+"----------------------");
            }
        } catch (IOException e) {
        }

    }
}
