package Stages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Methods {

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
        }
    }

    public static ArrayList<String> parseInput(String command){
        ArrayList<String> commandParts = new ArrayList<>();
        String sb="";
        boolean inQuotes = false;
        char quoteChar = '\0';

        for (char c : command.toCharArray()){
            if(c == '"' || c == '\''){
                if (inQuotes && c == quoteChar){
                    inQuotes = false;
                } 
                else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                }
                else {
                    sb+=c;
                }
            } else if (c == ' ' && !inQuotes) {
                if (sb.length() > 0){
                    commandParts.add(sb);
                    sb = "";
                }
            }
            else {
                sb+=c;
            }
        }
        if (sb.length() > 0){
            commandParts.add(sb);
        }
        return commandParts;
    }

    public static void executeScript(String scriptPath){
        try{
            List<String> scriptLines = Files.readAllLines(Paths.get(scriptPath));
            for (String element : scriptLines){
                if (element.trim().startsWith("//") || scriptLines.isEmpty()){
                    continue;
                }
                System.out.println("$" + element);
                List<String> commandParts = parseInput(element);
                if (commandParts.isEmpty()){
                    continue;
                }
                String command = commandParts.get(0);
                List<String> commandArgs = commandParts.subList(1, commandParts.size());
                System.out.println(command + " " + String.join(" ", commandArgs));
            }
        } catch (IOException e){
            System.out.println("Can't read script: " + e.getMessage());
        }
    }
}
