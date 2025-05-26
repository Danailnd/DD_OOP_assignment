import java.util.*;
import java.util.stream.Collectors;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = true;
    private static String specialtiesFilePath = null;
    private static String studentsFilePath = null;
    private static String studentSubjectsFilePath = null;
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
        System.out.println("1. Отвори файл (Open file)");
        System.out.println("2. Запази файл (Save file)");
        System.out.println("3. Запази като (Save as)");
        System.out.println("4. Запиши нов студент (Enroll)");
        System.out.println("5. Запиши студент в следващ курс (Advance)");
        System.out.println("6. Промени студент (Change)");
        System.out.println("7. Завърши студент (Graduate)");
        System.out.println("8. Прекъсни студент (Interrupt)");
        System.out.println("9. Възобнови студент (Resume)");
        System.out.println("10. Покажи информация за студент (print)");
        System.out.println("11. Покажи информация за всички студенти (print all)");
        System.out.println("12. Запиши студент в предмет (enrollin)");
        System.out.println("13. Дай оценка на студент за предмет (addgrade)");
        System.out.println("14. Извади протокол за предмет (protocol)");
        System.out.println("15. Извади академична справка за уценката на студент (report)");
        System.out.println("16. Помощ");
        System.out.println("17. Бързо отваряне (за тестване)");
        System.out.println("18. Изход");
    }
    private static void handleCommands(String command) {
        switch (command) {
            case "1": openFile(); break;
            case "2": saveFile(); break;
            case "3": saveAs(); break;
            case "4": enrollStudent(); break;
            case "5": handleAdvance(); break;
            case "6": changeStudent(); break;
            case "7": graduateStudent(); break;
            case "8": interruptStudent(); break;
            case "9": resumeStudent(); break;
            case "10": displayStudent(); break;
            case "11": displayStudents(); break;
            case "12": enrollStudentInSubject(); break;
            case "13": addGrade(); break;
            case "14": printProtocol(); break;
            case "15": printReport(); break;
            case "16": showHelp(); break;
            case "17": quickOpen(); break;
            case "18": exitApp(); break;
            default: System.out.println("Невалидна команда. Избери число от 1–18.");
        }
    }
    private static void enrollStudent() {
        System.out.print("Въведи факултетен номер: ");
        String fn = scanner.nextLine().trim();

        System.out.print("Въведи име на специалност: ");
        String programName = scanner.nextLine().trim();

        System.out.print("Въведи номер на група: ");
        int group;
        try {
            group = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Невалиден номер на група.");
            return;
        }

        System.out.print("Въведи име на студента: ");
        String name = scanner.nextLine().trim();

        try {
            Student newStudent = Student.enroll(name, fn, programName, group, specialties);
            students.add(newStudent);
            System.out.println(
                    "Успешно записан студент " + name +
                            " в 1 курс на специалност " + programName +
                            ", група " + group +
                            ", ФН: " + fn
            );
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void changeStudent() {
        System.out.print("Факултетен номер на студента: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        System.out.print("Опция за промяна (specialty, group, year): ");
        String option = scanner.nextLine().trim();

        System.out.print("Нова стойност: ");
        String value = scanner.nextLine().trim();

        try {
            student.applyChange(option, value, specialties);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void graduateStudent() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.graduate();
            System.out.println("Студентът е успешно отбелязан като завършил.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void interruptStudent() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.interrupt();
            System.out.println("Студентът е успешно отбелязан като прекъснал.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void resumeStudent() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        try {
            student.resume();
            System.out.println("Студентът е успешно отбелязан като записан.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void displayStudent() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        student.displayInfo();
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
    private static void handleAdvance(){
        System.out.print("Факултетен номер на студента: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        try {
            student.advanceToNextYear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void enrollStudentInSubject() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        System.out.print("Име на курс за прибавяне: ");
        String subjectName = scanner.nextLine().trim();

        Subject subject = student.getSpecialty().findSubjectByName(subjectName);

        if (subject == null) {
            System.out.println("Дисциплината не е част от специалността на студента.");
            return;
        }
        StudentSubject studentSubject = new StudentSubject(student, subject, -1);
        try {
            student.enrollInSubject(studentSubject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void addGrade() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        System.out.print("Име на курс: ");
        String courseName = scanner.nextLine().trim();

        StudentSubject enrollment = student.findEnrollmentBySubjectName(courseName);

        if (enrollment == null) {
            System.out.println("Студентът не е записан в дисциплината \"" + courseName + "\".");
            return;
        }

        System.out.print("Оценка: ");
        try {
            float grade = Float.parseFloat(scanner.nextLine().trim());
            enrollment.assignGrade(grade);
            System.out.println("Оценката е добавена успешно: " + courseName + " - " + grade);
        } catch (NumberFormatException e) {
            System.out.println("Невалидна стойност за оценка.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void printProtocol() {
        System.out.print("Име на дисциплина: ");
        String courseName = scanner.nextLine().trim();

        List<Subject> allSubjects = specialties.stream()
                .flatMap(specialty -> specialty.getCourses().stream())
                .toList();
        Subject subject = Subject.findByName(allSubjects, courseName);
        if (subject == null) {
            System.out.println("Не е намерена дисциплина с име: " + courseName);
            return;
        }
        subject.printProtocol(students);
    }
    private static void printReport() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        student.printAcademicReport();
    }
    private static void openFile() {
        System.out.print("Път на файл с специалности: ");
        specialtiesFilePath = scanner.nextLine().trim();
        specialties = Specialty.loadFromUserInput(specialtiesFilePath);
        System.out.print("Път на файл със студенти: ");
        studentsFilePath = scanner.nextLine().trim();
        students = Student.loadFromUserInput(studentsFilePath, specialties);
        System.out.print("Път на файл със студентски предмети: ");
        studentSubjectsFilePath = scanner.nextLine().trim();
        StudentSubject.loadFromUserInput(
                studentSubjectsFilePath, students, specialties
        );
    }
    private static void quickOpen() {
        specialtiesFilePath = "data/specialties.json";
        studentsFilePath = "data/students.json";
        studentSubjectsFilePath = "data/studentSubjects.json";

        specialties = Specialty.loadFromUserInput(specialtiesFilePath);
        students = Student.loadFromUserInput(studentsFilePath, specialties);
        StudentSubject.loadFromUserInput(studentSubjectsFilePath, students, specialties);

        System.out.println("Данните са заредени от preset файлове:");
        System.out.println(" - Специалности: " + specialtiesFilePath);
        System.out.println(" - Студенти: " + studentsFilePath);
        System.out.println(" - Студентски предмети: " + studentSubjectsFilePath);
    }
    private static void saveFile() {
        if (specialtiesFilePath == null || studentsFilePath == null || studentSubjectsFilePath == null) {
            System.out.println("Не са намерени заредени файлове. Моля използвайте 'Запази като' за запазване в нова директория.");
            return;
        }
        try {
            Specialty.saveToFile(specialties, specialtiesFilePath);
            Student.saveToFile(students, studentsFilePath);
            List<StudentSubject> allStudentSubjects = students.stream()
                    .flatMap(student -> student.getEnrolledSubjects().stream())
                    .collect(Collectors.toList());
            StudentSubject.saveToFile(allStudentSubjects, studentSubjectsFilePath);
            System.out.println("Успешно записани всички данни.");
        } catch (RuntimeException e) {
            System.out.println("Възникна грешка при записването: " + e.getMessage());
        }
    }
    private static void saveAs() {
        System.out.print("Път за запазване на специалностите: ");
        specialtiesFilePath = scanner.nextLine().trim();

        System.out.print("Път за запазване на студентите: ");
        studentsFilePath = scanner.nextLine().trim();

        System.out.print("Път за запазване на студентските предмети: ");
        studentSubjectsFilePath = scanner.nextLine().trim();

        saveFile();
    }
    private static void showHelp() {
        System.out.println("\n--- Помощна информация за командите ---");
        System.out.println("1. Отвори файл (Open file) - Зарежда специалности, студенти и студентски дисциплини от файлове.");
        System.out.println("2. Запази файл (Save file) - Запазва текущите данни във файловете.");
        System.out.println("3. Запази като (Save as) - Запазва текущите данни във файл по ваш избор.");
        System.out.println("4. Запиши нов студент (Enroll) - Параметри: факултетен номер, специалност, група, име. Записва нов студент в 1 курс.");
        System.out.println("5. Запиши студент в следващ курс (Advance) - Параметър: факултетен номер. Прехвърля студент към следващ курс при условия.");
        System.out.println("6. Промени студент (Change) - Параметри: факултетен номер, опция (specialty, group, year), нова стойност. Променя данни за студент.");
        System.out.println("7. Завърши студент (Graduate) - Параметър: факултетен номер. Отбелязва студент като завършил при успешно положени изпити.");
        System.out.println("8. Прекъсни студент (Interrupt) - Параметър: факултетен номер. Отбелязва студент като прекъснал обучението.");
        System.out.println("9. Възобнови студент (Resume) - Параметър: факултетен номер. Възстановява студент със статус записан.");
        System.out.println("10. Покажи информация за студент (Print) - Параметър: факултетен номер. Показва данни за конкретен студент.");
        System.out.println("11. Покажи информация за всички студенти (Print all) - Извежда списък с всички студенти.");
        System.out.println("12. Запиши студент в предмет (Enrollin) - Параметри: факултетен номер, име на курс. Записва студент в дисциплина.");
        System.out.println("13. Дай оценка на студент за предмет (Addgrade) - Параметри: факултетен номер, име на курс, оценка. Добавя или променя оценка.");
        System.out.println("14. Извади протокол за предмет (Protocol) - Параметър: име на дисциплина. Извежда списък на записаните студенти с оценки.");
        System.out.println("15. Извади академична справка за успеха на студент (Report) - Параметър: факултетен номер. Показва академична справка с оценки и среден успех.");
        System.out.println("16. Помощ (Help) - Показва тази помощна информация.");
        System.out.println("17. Изход (Exit) - Прекратява програмата.");
    }
    private static void exitApp() {
        System.out.println("Излизане от приложението...");
        running = false;
    }
}
