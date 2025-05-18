import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean running = true;

    private static String filePath = null;

    public static void main(String[] args) {
        while (running) {
            printCommands();
            System.out.print("Избери команда: ");
            String input = scanner.nextLine().trim();
            handleCommands(input);
        }
    }

    private static void printCommands() {
        System.out.println("\n--- Меню за Управление на Студенти ---");
        System.out.println("1. Отвори файл");
        System.out.println("2. Запази файл");
        System.out.println("3. Запази като");
        System.out.println("8. Помощ");
        System.out.println("9. Изход");
    }

    private static void handleCommands(String command) {
        switch (command) {
            case "1": openFile(); break;
            case "2": saveFile(); break;
            case "3": saveAs(); break;
            case "8": showHelp(); break;
            case "9": exitApp(); break;
            default: System.out.println("Невалидна команда. Избери число от 1–9.");
        }
    }
    private static void openFile() {
        System.out.print("Път на файла: ");
        filePath = scanner.nextLine().trim();
        // TODO:
        System.out.println("Отваря се файл: " + filePath);
    }

    private static void saveFile() {
        if (filePath == null) {
            System.out.println("Не е намерен файл со това име, създава се нов файл.");
            return;
        }
        // TODO:
        System.out.println("Запазено като: " + filePath);
    }

    private static void saveAs() {
        System.out.print("Път на файла: ");
        filePath = scanner.nextLine().trim();
        saveFile();
    }

    private static void showHelp() {
        System.out.println("Помощ");
    }

    private static void exitApp() {
        System.out.println("Излизане от приложението...");
        running = false;
    }
}
