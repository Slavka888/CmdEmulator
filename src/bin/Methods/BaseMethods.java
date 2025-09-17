package bin.Methods;

import java.io.File;
import java.util.Scanner;

public class BaseMethods {
    public static String inputCommand() {
        String command;
        Scanner scanner = new Scanner(System.in);
        command = scanner.nextLine();
        return command;
    }

    public static void getUserName() {
        String username = System.getProperty("user.name");
        System.out.print(username + ":~#");
    }

    public static void getCurrentFiles() {
        String folderPath = ".";//проверить этот момент
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            String[] files = folder.list();

            if (files != null) {
                System.out.println("File names: ");
                for (String file : files) {
                    System.out.print(file + " ");
                }
            } else {
                System.out.print("Empty folder");
            }
        } else {
            System.out.print("Error");
        }
    }

    public static void parseCd(String command) {
        String[] arrCmd = command.split(" ");
        if (arrCmd.length == 1) {
            changeDirectory(arrCmd[0]);
            return;
        }
        if (arrCmd.length ==2){
            changeDirectory(arrCmd[1]);
            return;
        }
    }

    public static void changeDirectory(String direct){
        if (!direct.equals("cd")){
            String newDirectPath = direct;
            try {
                File newDirect = new File(newDirectPath);

                if(!newDirect.exists()){
                    System.out.println("Directory doesn't exist. Creating...");
                    if (newDirect.mkdirs()){
                        System.out.print("New directory was created: " + newDirect.getAbsolutePath());
                    }
                    else {
                        System.out.print("Can't create new directory");
                        return;
                    }
                }

                if (!newDirect.isDirectory()){
                    System.out.print("Current path can't be a directory");
                    return;
                }

                System.setProperty("user.dir", newDirect.getAbsolutePath());
                String currentDirect = System.getProperty("user.dir");
                System.out.print("Current directory: " + currentDirect);

            }
            catch (Exception e){
                System.out.print("Error" + e.getMessage());
            }
        }
        else {
            try {

                String newDirectPath = "D:/prog/java/ConfigProject/CmdEmulator";
                File newDirect = new File(newDirectPath);
                if (!newDirect.isDirectory()) {
                    System.out.print("Current path can't be a directory");
                    return;
                }
                System.setProperty("user.dir", newDirect.getAbsolutePath());
                String currentDirect = System.getProperty("user.dir");
                System.out.print("Current directory: " + currentDirect);

            }
            catch (Exception e){
                System.out.print("Error" + e.getMessage());
            }
        }
    }

    public static void executeCommand(String command) {
        if (command.equals("ls")) {
            getCurrentFiles();
            System.out.println();
            return;
        }
        if (command.split(" ")[0].equals("cd")) {
            parseCd(command);
            System.out.println();
            return;
        }
        System.out.println("Unknown command");
    }
}

