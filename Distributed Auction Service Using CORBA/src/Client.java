import P3.info;
import P3.infoHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Scanner;

/**
 * Created by chenguanghe on 3/30/15.
 */
public class Client {
    static info infoImpl;
    private static String role = null;
    private static Scanner in = new Scanner(System.in);
    private static String username;
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "info";
            infoImpl = infoHelper.narrow(ncRef.resolve_str(name));
            System.out.println("Please enter user name: ");
            username = in.next();
            boolean run = true;
            while (run) {
                selectRoles();
                if (role.equals("seller")){
                    seller(infoImpl);
                }
                if (role.equals("bidder")){
                    bidder(infoImpl);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void seller(info infoImpl){
        boolean run = true;
        while (run) {
            sellerMainMenu();
            String input = in.next();
            switch (input){
                case "1" :{
                    System.out.println("Please enter item name:");
                    String itemname = in.next();
                    System.out.println("Please enter price:");
                    String price = in.next();
                    StringHolder s = new StringHolder();
                    infoImpl.sell(username, itemname, Integer.valueOf(price), s);
                    System.out.println(s.value);
                    continue;
                }
                case "2" :{
                    StringHolder name = new StringHolder();;
                    StringHolder price = new StringHolder();;
                    infoImpl.view_high_bidder(username,name, price);
                    if (price.value.equals("-1"))
                        System.out.println(name.value);
                    else
                        System.out.println("Current highest bidder is " + name.value+"  and price is " + price.value);
                    continue;
                }
                case "3":{
                    StringHolder s = new StringHolder();;
                    infoImpl.done(username,s);
                    System.out.println(s.value);
                    continue;
                }
                case "4":{
                    StringHolder s = new StringHolder();;
                    infoImpl.view_item(username, s);
                    System.out.println(s.value);
                    continue;
                }
                case "5" :{
                    return;
                }
                default: continue;
            }
        }
    }
    private static void bidder(info infoImpl){
        boolean run = true;
        while (run) {
            bidderMainMenu();
            String input = in.next();
            switch (input){
                case "1" :{
                    System.out.println("Please enter price:");
                    String price = in.next();
                    StringHolder s = new StringHolder();;
                    infoImpl.bid(username, Integer.valueOf(price), s);
                    System.out.println(s.value);
                    continue;
                }
                case "2" :{
                    StringHolder name = new StringHolder();;
                    StringHolder price = new StringHolder();;
                    infoImpl.view_high_bidder(username,name, price);
                    if (price.value.equals("-1"))
                        System.out.println(name.value);
                    else
                        System.out.println("Current highest bidder is " + name.value+"  and price is " + price.value);
                    continue;
                }
                case "3":{
                    StringHolder s = new StringHolder();;
                    infoImpl.view_item(username, s);
                    System.out.println(s.value);
                    continue;
                }
                case "4" :{
                    return;
                }
                default: continue;
            }
        }
    }
    private static void sellerMainMenu(){
        System.out.println("");
        System.out.println("1. Offer Item");
        System.out.println("2. View Highest Bidder");
        System.out.println("3. Sell");
        System.out.println("4. View Auction Status");
        System.out.println("5. Quit");
    }
    private static void bidderMainMenu(){
        System.out.println("");
        System.out.println("1. Bid");
        System.out.println("2. View Bid Status");
        System.out.println("3. View Auction Status");
        System.out.println("4. Quit");
    }
    private static void selectRoles(){
        boolean run = true;
        while (run) {
            System.out.println("Welcome to Auction Client");
            System.out.println("Please select your role:");
            printRoles();
            String input = in.next();
            switch (input){
                case "1" : {
                    role = "seller";
                    run = !run;
                    break;
                }
                case "2" : {
                    role = "bidder";
                    run = !run;
                    break;
                }
                case "3" :{
                    return;
                }
            }
        }
    }
    private static void printRoles(){
        System.out.println("");
        System.out.println("1. Seller");
        System.out.println("2. Bidder");
        System.out.println("3. Quit");

    }
}
