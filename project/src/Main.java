import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean running = true;
    private static String filePath = null;
    private static String specialitesFilePath = null;
    private static String studentsFilePath = null;
    private static List<Student> students = new ArrayList<>();
    private static List<Specialty> specialties = new ArrayList<>();

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
        System.out.print("Път на файл с специалности: ");
        specialitesFilePath = scanner.nextLine().trim();
        List<Specialty> loaded = JsonDeserializeHelper.loadSpecialtiesFromFile(specialitesFilePath);
        if (loaded == null) {
            System.out.println("Неуспешно зареждане на специалности.");
        } else {
            specialties = loaded;
            System.out.println("Успешно заредени " + specialties.size() + " специалности.");
        }
        System.out.print("Път на файл със студенти: ");
        studentsFilePath = scanner.nextLine().trim();
        List<Student> loadedStudents = JsonDeserializeHelper.loadStudentsFromFile(studentsFilePath, specialties);

        if (loadedStudents == null) {
            System.out.println("Неуспешно зареждан1е на студенти.");
        } else {
            students = loadedStudents;
            System.out.println("Успешно заредени " + students.size() + " студенти.");
        }
    }

    private static void displaySpecialties(){
        if(specialties.isEmpty()){
            System.out.println("Няма заредени специалности.");
            return;
        }
        System.out.println("Списък на специалностите:");
        for (Specialty sp : specialties) {
            System.out.println("- " + sp.getName());
            for (Subject subject : sp.getCourses()) {
                System.out.printf("  * %s (Задължителен: %b, Възможни курсове: %s)%n",
                        subject.getName(), subject.isMandatory(), subject.getAvailableYears());
            }
        }
    }

    private static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("Няма заредени студенти.");
            return;
        }
        System.out.println("Списък на студентите:");
        for (Student s : students) {
            System.out.printf("- Име: %s | Факултетен номер: %s | Специалност: %s | Курс: %d | Група: %d | Статус: %s | Среден успех: %.2f%n",
                    s.getName(),
                    s.getFacultyNumber(),
                    (s.getSpecialty() != null ? s.getSpecialty().getName() : "Неизвестна"),
                    s.getCourse(),
                    s.getGroup(),
                    s.getStatus(),
                    s.getAverageGrade()
            );
        }
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
