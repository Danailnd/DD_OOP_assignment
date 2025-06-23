import java.util.Scanner;

public abstract class Operation {

    public static DataStore g_data_store;
    public static Scanner scanner;

    public static boolean shouldExit = false;

    public void execute(String[] args) {

    }
    public static void setScanner(Scanner s) {
        scanner = s;
    }
}
