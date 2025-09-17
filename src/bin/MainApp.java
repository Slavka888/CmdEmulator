package bin;

import java.util.Scanner;

import static bin.Methods.BaseMethods.*;


public class MainApp{
    public static void startSystem(){
        System.out.println("---WELCOME TO THE OS EMULATOR---\n" + "WANT START?\n" + "YES/NO");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.toLowerCase().equals("no")){
            System.out.println("Bye!");
            System.exit(0);
        }
        else {
            System.out.println("Let's start!");
            getUserName();
        }
    }
    public static void main(String[] args) {
        startSystem();
        while(true){
            String command = inputCommand();
            if (command.equals("exit")) {break;}
            executeCommand(command);
            getUserName();
        }
    }
}

