package Stages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import static Stages.Emulator.*;

public class Methods {

    public static void startSystem() {
        System.out.println("---WELCOME TO THE OS EMULATOR---\n" + "WANT START?\n" + "YES/NO");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.toLowerCase().equals("no")) {
            System.out.println("Bye!");
            System.exit(0);
        } else {
            System.out.println("Let's start!");
        }
    }

    public static ArrayList<String> parseInput(String command) {
        ArrayList<String> commandParts = new ArrayList<>();
        String sb = "";
        boolean inQuotes = false;
        char quoteChar = '\0';

        for (char c : command.toCharArray()) {
            if (c == '"' || c == '\'') {
                if (inQuotes && c == quoteChar) {
                    inQuotes = false;
                } else if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                } else {
                    sb += c;
                }
            } else if (c == ' ' && !inQuotes) {
                if (sb.length() > 0) {
                    commandParts.add(sb);
                    sb = "";
                }
            } else {
                sb += c;
            }
        }
        if (sb.length() > 0) {
            commandParts.add(sb);
        }
        return commandParts;
    }

    public static void executeScript(String scriptPath) {
        try {
            List<String> scriptLines = Files.readAllLines(Paths.get(scriptPath));
            for (String element : scriptLines) {
                if (element.trim().startsWith("//") || scriptLines.isEmpty()) {
                    continue;
                }
                System.out.println("$" + element);
                List<String> commandParts = parseInput(element);
                if (commandParts.isEmpty()) {
                    continue;
                }
                String command = commandParts.get(0);
                List<String> commandArgs = commandParts.subList(1, commandParts.size());
                System.out.println(command + " " + String.join(" ", commandArgs));
                if (command.toLowerCase().equals("exit")) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read script: " + e.getMessage());
        }
    }

    static void loadVFS(String pathVFS) {
        try {
            //создание парсера xml фала
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathVFS);
            Element rootElem = doc.getDocumentElement();//получаем корневой элемент
            root.name = "/";
            root.isDirectory = true;
            parseXML(rootElem, root);
        } catch (Exception e) {
            System.out.println("Can't load VFS: " + e.getMessage());
        }
    }

    //Рекурсивный парсинг XML элемента и строительство соответствующей структуры виртуальной файловой системы
    static void parseXML(Element xml, VFSNode vfs) {
        //Получаем все дочерние узлы текущего XML элемента
        NodeList children = xml.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            //Проверяем, что узел является элементом
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                VFSNode newNode = new VFSNode();
                newNode.name = child.getAttribute("name");
                //Обрабатываем директорию
                if (child.getTagName().equals("dir")) {
                    newNode.isDirectory = true;
                    vfs.children.put(newNode.name, newNode);
                    parseXML(child, newNode);
                //Обрабатываем файл
                } else if (child.getTagName().equals("file")) {
                    newNode.isDirectory = false;
                    //Получаем содержимое файла в формате Base64 из текстового содержимого XML элемента
                    String base64 = child.getTextContent();
                    //Декодируем Base64 строку в бинарные данные и сохраняем в содержимое узла
                    newNode.content = Base64.getDecoder().decode(base64);
                    vfs.children.put(newNode.name, newNode);
                }
            }
        }
    }

    //метод для выполнения команды ls
    static void doCommandL(List<String> commandArgs){
        VFSNode current = getNodeByPath(currentPath);
        if (current == null || !current.isDirectory){
            System.out.println("Is's not a directory");
            return;
        }
        for (String child : current.children.keySet()){
            System.out.println(child);
        }
    }

    static void doCommandCd(List<String> commandArgs){
        if (commandArgs.isEmpty()){
            currentPath = "/";
            System.out.println(currentPath);
            return;
        }
        String newPath = setPath(commandArgs.get(0));
        VFSNode newNode = getNodeByPath(newPath);
        if (newNode!=null && newNode.isDirectory){
            currentPath = newPath;
            System.out.println(currentPath);
        }
        else{
            System.out.println("No such directory");
        }
    }

    static void doCommandDu(List<String> commandArgs){
        String path;
        if (commandArgs.isEmpty()){
            path = currentPath;
        }
        else {
            path = setPath(commandArgs.get(0));
        }
        VFSNode newNode = getNodeByPath(path);
        if (newNode == null){
            System.out.println("No such file or directory");
            return;
        }
        long size = calculateSize(newNode);
        System.out.println(size);
    }

    static void doCommandUname(){
        System.out.println("Linux emulated");
    }

    static void doCommandPwd(){
        System.out.println(currentPath);
    }

    static VFSNode getNodeByPath(String path){
        VFSNode current = root;
        String[] parts = path.split("/");
        for (String part : parts){
            if (part.isEmpty() || part.equals(".")){
                continue;
            }
            if (part.equals("..")){
                continue;
            }
            current = current.children.get(part);
            if (current == null){
                return null;
            }
        }
        return current;
    }

    static String setPath(String path){
        if (path.equals("..")){
            return currentPath.substring(0, currentPath.lastIndexOf("/"));
        }
        if (path.startsWith("/")){
            return path;
        }
        return currentPath + (currentPath.endsWith("/")? "" : "/") + path;
    }

    static long calculateSize(VFSNode node){
        if (!node.isDirectory){
            return node.content != null ? node.content.length : 0;
        }
        long size = 0;
        for (VFSNode child : node.children.values()){
            size+= calculateSize(child);
        }
        return size;
    }
}
