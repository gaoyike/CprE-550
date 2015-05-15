import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.Naming;
import java.util.Scanner;

/**
 * Created by chenguanghe on 2/20/15.
 */
public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static String username = "";
    private static String ChatRoom = "";
    private static String Server = "localhost";
    private static String Client = "localhost";

    public static void main(String[] args) {
        String input;
        try {
            System.out.println("Welcome to ChatRoom App!");
            System.out.println();
            System.out.println("Please enter the Server address, or press enter localhost to use localhost");
            input = getUserInput();
            Server = input;
            System.out.println("Please enter your name:");
            input = getUserInput();
            username = input;
            Message msgtoserver = (Message) Naming.lookup("Server");
            while (true) {
                MainMenu();
                input = getUserInput();
                if (input.equals("1")) while (true) {
                    ServerMenu();
                    input = getUserInput();
                    if (input.equals("1")) {
                        String[] list = msgtoserver.ChatRoomList();
                        if (list.length == 0) {
                            System.out.println("There is no chat room now!");
                            continue;
                        } else
                            ListChatRoomMenu(list);
                        input = getUserInput();
                        if (input.equals("s"))
                            continue;
                        if (input.equals("q"))
                            break;
                        String[] info = list[Integer.valueOf(input) - 1].split("\\|"); // chatroom name|port
                        if (msgtoserver.JoinChatRoom(info[0], username, Client)) {
                            // chat in here
                            Socket socket = new Socket(Server, Integer.parseInt(info[1]));
                            PrintWriter out =
                                    new PrintWriter(socket.getOutputStream(), true);
                            BufferedReader in =
                                    new BufferedReader(
                                            new InputStreamReader(socket.getInputStream()));
                            BufferedReader stdIn =
                                    new BufferedReader(
                                            new InputStreamReader(System.in));
                            System.out.println("Connection Succeed!");
                            PrintInput printInput = new PrintInput(in, out);      // load server chat msgs
                            printInput.start();
                            out.println(username);
                            String userInput;
                            while (true) {
                                while ((userInput = stdIn.readLine()) != null && userInput.length() != 0) {
                                    if (userInput.equals("!q")) {
                                        out.println("!q");
                                        printInput.interrupt();
                                        msgtoserver.LeaveChatRoom(info[0], username);
                                        break;
                                    }
                                    out.println(userInput);
                                    userInput = null;
                                }
                                if (userInput.equals("!q"))
                                    break;
                            }
                        }
                    } else if (input.equals("2")) {
                        System.out.println("Please enter chat room name:");
                        input = getUserInput();
                        if (msgtoserver.CreateChatRoom(input))
                            System.out.println("CharRoom " + input + " has been created!");
                    } else if (input.equals("3")) {
                        System.out.println("Please enter chat room name:");
                        input = getUserInput();
                        if (msgtoserver.DeleteChatRoom(input))
                            System.out.println("CharRoom " + input + " has been created!");
                    } else if (input.equals("4")) {
                        break;
                    } else if (input.equals("q")) {
                        return;
                    } else {
                        System.out.println("Unsupported Selection!");
                    }
                }
                else if (input.equals("q")) {
                    System.out.println("See Ya!");
                    return;
                } else {
                    System.out.println("Unsupported Selection!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MainMenu() {
        System.out.println("Options:");
        System.out.println("(1) Join Char Server");
        System.out.println("(q) Quit");
    }

    private static void ServerMenu() {
        System.out.println("Options:");
        System.out.println("(1) List Chat Room");
        System.out.println("(2) Create Chat Room");
        System.out.println("(3) Delete Chat Room");
        System.out.println("(4) Go to Main MainMenu");
        System.out.println("(q) Quit");
    }

    private static void ListChatRoomMenu(String[] list) {
        System.out.println("Press the number to join ChatRoom");
        System.out.println("Options:");
        int i = 1;
        for (String s : list) {
            String t[] = s.split("\\|");
            System.out.println("(" + i + ") " + t[0]);
            i++;
        }
        i++;
        System.out.println("(s) Go to ServerMenu");
        System.out.println("(q) Quit");
    }

    private static String getUserInput() {
        return scanner.next();
    }


}
