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

    private static List<StudentSubject> studentSubjects = new ArrayList<>();

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
        System.out.println("17. Изход");
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
            case "17": exitApp(); break;
            default: System.out.println("Невалидна команда. Избери число от 1–12.");
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
            student.applyChange(option, value, specialties, studentSubjects);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void advance(int targetYear, Student student) {
        try {
            student.advanceToNextYear(targetYear, studentSubjects);
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
            student.graduate(studentSubjects);
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

        advance(student.getCourse()+1,student);
    }
    private static void enrollStudentInSubject() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = students.stream()
                .filter(s -> s.getFacultyNumber().equals(fn))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        if (student.getStatus() == StudentStatus.SUSPENDED) {
            System.out.println("Студентът е с прекъснато обучение и не може да записва дисциплини.");
            return;
        }

        if (student.getSpecialty() == null) {
            System.out.println("Студентът няма зададена специалност.");
            return;
        }
        System.out.print("Име на курс за прибавяне: ");
        String courseName = scanner.nextLine().trim();

        Subject subject = student.getSpecialty().getCourses().stream()
                .filter(s -> s.getName().equalsIgnoreCase(courseName))
                .findFirst()
                .orElse(null);

        if (subject == null) {
            System.out.println("Дисциплината не е част от специалността на студента.");
            return;
        }

        if (!subject.getAvailableYears().contains(student.getCourse())) {
            System.out.println("Дисциплината не се предлага за курс " + student.getCourse() + ".");
            return;
        }

        boolean alreadyEnrolled = studentSubjects.stream()
                .anyMatch(ss -> ss.getStudent().equals(student) && ss.getSubject().equals(subject));

        if (alreadyEnrolled) {
            System.out.println("Студентът вече е записан в тази дисциплина.");
            return;
        }

        StudentSubject _studentSubject = new StudentSubject(student, subject, -1);
        studentSubjects.add(_studentSubject);
        student.enrollInSubject(_studentSubject);
        System.out.println("Успешно записване на дисциплината: " + subject.getName());
    }
    private static void addGrade(){
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = students.stream()
                .filter(s -> s.getFacultyNumber().equals(fn))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }
        if (student.getStatus() == StudentStatus.SUSPENDED) {
            System.out.println("Студентът е прекъснал и не може да се явява на изпити.");
            return;
        }

        System.out.print("Оценка: ");
        String gradeStr = scanner.nextLine().trim();
        float grade;
        try {
            grade = Float.parseFloat(gradeStr);
            if (grade < 2.0 || grade > 6.0) {
                System.out.println("Оценката трябва да е между 2.00 и 6.00.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Невалидна стойност за оценка.");
            return;
        }

        System.out.print("Име на курс: ");
        String courseName = scanner.nextLine().trim();

        StudentSubject enrollment = studentSubjects.stream()
                .filter(ss -> ss.getStudent().equals(student) &&
                        ss.getSubject().getName().equalsIgnoreCase(courseName))
                .findFirst()
                .orElse(null);

        if (enrollment == null) {
            System.out.println("Студентът не е записан в дисциплината \"" + courseName + "\".");
            return;
        }

        enrollment.setGrade(grade);
       student.recalculateAverage();
        System.out.println("Оценката е добавена успешно: " + courseName + " - " + grade);
    }
    private static void printProtocol() {
        System.out.print("Име на дисциплина: ");
        String courseName = scanner.nextLine().trim();

        List<StudentSubject> enrolledInCourse = studentSubjects.stream()
                .filter(ss -> ss.getSubject().getName().equalsIgnoreCase(courseName))
                .toList();

        if (enrolledInCourse.isEmpty()) {
            System.out.println("Няма записани студенти в дисциплината \"" + courseName + "\".");
            return;
        }

        Map<String, Map<Integer, List<StudentSubject>>> grouped =
                enrolledInCourse.stream()
                        .collect(Collectors.groupingBy(
                                ss -> ss.getStudent().getSpecialty().getName(),
                                Collectors.groupingBy(ss -> ss.getStudent().getCourse())
                        ));

        System.out.print("\n--- Протокол за дисциплина ---");
        for (String specialty : grouped.keySet()) {
            Map<Integer, List<StudentSubject>> byYear = grouped.get(specialty);
            for (Integer year : byYear.keySet()) {
                List<StudentSubject> list = byYear.get(year).stream()
                        .sorted(Comparator.comparing(ss -> ss.getStudent().getFacultyNumber()))
                        .toList();



                for (StudentSubject ss : list) {
                    Student s = ss.getStudent();
                    String gradeStr = ss.getGrade() > 0 ? String.format("%.2f", ss.getGrade()) : "Няма оценка";
                    System.out.printf("ФН: %s | Име: %s | Група: %d | Оценка: %s%n",
                            s.getFacultyNumber(), s.getName(), s.getGroup(), gradeStr);
                }
            }
        }
    }
    private static void printReport() {
        System.out.print("Факултетен номер: ");
        String fn = scanner.nextLine().trim();

        Student student = students.stream()
                .filter(s -> s.getFacultyNumber().equals(fn))
                .findFirst()
                .orElse(null);

        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        List<StudentSubject> records = studentSubjects.stream()
                .filter(ss -> ss.getStudent().equals(student))
                .toList();

        if (records.isEmpty()) {
            System.out.println("Няма записани дисциплини за този студент.");
            return;
        }

        List<StudentSubject> passed = new ArrayList<>();
        List<StudentSubject> missing = new ArrayList<>();

        for (StudentSubject ss : records) {
            if (ss.getGrade() >= 3.0) {
                passed.add(ss);
            } else {
                missing.add(ss);
            }
        }

        System.out.println("\n--- Академична справка за " + student.getName() + " (ФН: " + fn + ") ---");

        if (!passed.isEmpty()) {
            System.out.println("\n Взети изпити:");
            for (StudentSubject ss : passed) {
                System.out.printf("• %s - %.2f%n", ss.getSubject().getName(), ss.getGrade());
            }
        }

        if (!missing.isEmpty()) {
            System.out.println("\n Невзети:");
            for (StudentSubject ss : missing) {
                System.out.printf("• %s%n", ss.getSubject().getName());
            }
        }
        System.out.printf("%n Среден успех: %.2f%n", student.getAverageGrade());
    }
    private static void openFile() {
        System.out.print("Път на файл с специалности: ");
        specialtiesFilePath = scanner.nextLine().trim();
        List<Specialty> specialties = Specialty.loadFromUserInput(specialtiesFilePath);
        System.out.print("Път на файл със студенти: ");
        studentsFilePath = scanner.nextLine().trim();
        students = Student.loadFromUserInput(studentsFilePath, specialties);
        System.out.print("Път на файл със студентски предмети: ");
        studentSubjectsFilePath = scanner.nextLine().trim();
        studentSubjects = StudentSubject.loadFromUserInput(
                studentSubjectsFilePath, students, specialties
        );
    }
    private static void saveFile() {
        if (specialtiesFilePath == null || studentsFilePath == null || studentSubjectsFilePath == null) {
            System.out.println("Не са намерени заредени файлове. Моля използвайте 'Запази като' за запазване в нова директория.");
            return;
        }
        try {
            Specialty.saveToFile(specialties, specialtiesFilePath);
            Student.saveToFile(students, studentsFilePath);
            StudentSubject.saveToFile(studentSubjects, studentSubjectsFilePath);
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
