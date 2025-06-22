import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class University {
    private static final Scanner scanner = new Scanner(System.in);
    private static String specialtiesFilePath = null;
    private static String studentsFilePath = null;
    private static String studentSubjectsFilePath = null;
    private static List<Student> students = new ArrayList<>();
    private static List<Specialty> specialties = new ArrayList<>();
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
}
