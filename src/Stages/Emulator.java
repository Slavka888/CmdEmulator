package Stages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Stages.Methods.*;

public class Emulator {
    static String pathVFS;
    static String pathStartScript;
    public static void main(String[] args) {
        //обрабатываем аргументы командной строки
        switch (args.length){
            case 2:
                if (args[0].equals("--vfs")) {
                    pathVFS = args[1];
                    break;
                }
                else {
                    System.out.println("VFS path not set");
                    System.exit(1);
                }
            case 3:
                if (args[0].equals("--vfs")) {
                    pathVFS = args[1];
                    System.out.println("StartScript not set");
                    break;
                }
                else {
                    System.out.println("VFS path not set");
                    System.exit(1);
                }
            case 4:
                if (args[0].equals("--vfs")) {
                    pathVFS = args[1];
                }
                else {
                    System.exit(1);
                }
                if (args[2].equals("--script")) {
                    pathStartScript = args[3];
                    break;
                }
                System.out.println("ScriptStart path not set");
                break;
            default:
                System.out.println("Unknown parameter");
//                System.exit(1);
                break;
        }
//        pathStartScript = "D:\\prog\\java\\ConfigProject\\CmdEmulator\\for_tasks\\script.txt";
        System.out.println("VFS path: " + pathVFS);
        System.out.println("StartScript path: " + pathStartScript);

        if (pathStartScript != null){
            executeScript(pathStartScript);
        }
        //приветственное меню
        startSystem();
        //начинаем считывание команд, настраиваем вывод
        String username = System.getProperty("user.name");
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            //Получаем объект InetAddress, представляющий текущий компьютер
            //После этого извлекаем имя хоста
        } catch (UnknownHostException e) {
            hostname = "hostname";
        }
        String currentDir = "~";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(username + "@" + hostname + ":" + currentDir);
            String inputCommand;
            try {
                inputCommand = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }
            if (inputCommand == null || inputCommand.trim().isEmpty()) {
                continue;
            }
            ArrayList<String> commandParts = parseInput(inputCommand);
            if (commandParts.isEmpty()) {
                continue;
            }
            String command = commandParts.get(0);
            List<String> commandArgs = commandParts.subList(1, commandParts.size());
            //вывод выбранной команды и аргументов к ней
            switch (command){
                case "ls":
                    System.out.println("ls " + String.join(" ", commandArgs));
                    break;
                case "cd":
                    System.out.println("cd " + String.join(" ",commandArgs));
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
    }
}
