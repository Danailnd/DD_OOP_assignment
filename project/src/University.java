import java.util.List;

public class University {

    private static void enrollStudent() {
        System.out.print("Въведи факултетен номер: ");
        String fn = Operation.scanner.nextLine().trim();

        System.out.print("Въведи име на специалност: ");
        String programName = Operation.scanner.nextLine().trim();

        System.out.print("Въведи номер на група: ");
        int group;
        try {
            group = Integer.parseInt(Operation.scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Невалиден номер на група.");
            return;
        }

        System.out.print("Въведи име на студента: ");
        String name = Operation.scanner.nextLine().trim();

        try {
            Student newStudent = Student.enroll(name, fn, programName, group, Operation.g_data_store.specialties);
            Operation.g_data_store.students.add(newStudent);
            System.out.printf("Успешно записан студент %s в 1 курс на специалност %s, група %d, ФН: %s%n", name, programName, group, fn);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void changeStudent() {
        System.out.print("Факултетен номер на студента: ");
        String fn = Operation.scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        System.out.print("Опция за промяна (specialty, group, year): ");
        String option = Operation.scanner.nextLine().trim();

        System.out.print("Нова стойност: ");
        String value = Operation.scanner.nextLine().trim();

        try {
            student.applyChange(option, value, Operation.g_data_store.specialties);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void graduateStudent() {
        System.out.print("Факултетен номер: ");
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
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
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
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
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
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
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        student.displayInfo();
    }

    private static void displayStudents() {
        if (Operation.g_data_store.students.isEmpty()) {
            System.out.println("Няма заредени студенти.");
            return;
        }

        System.out.println("Списък на студентите:");
        for (Student s : Operation.g_data_store.students) {
            System.out.printf("- Име: %s | Факултетен номер: %s | Специалност: %s | Курс: %d | Група: %d | Статус: %s | Среден успех: %.2f%n",
                    s.getName(),
                    s.getFacultyNumber(),
                    (s.getSpecialty() != null ? s.getSpecialty().getName() : "Неизвестна"),
                    s.getCourse(),
                    s.getGroup(),
                    s.getStatus(),
                    s.getAverageGrade());
        }
    }

    private static void handleAdvance() {
        System.out.print("Факултетен номер на студента: ");
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
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
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        System.out.print("Име на курс за прибавяне: ");
        String subjectName = Operation.scanner.nextLine().trim();
        Subject subject = student.getSpecialty().findSubjectByName(subjectName);
        if (subject == null) {
            System.out.println("Дисциплината не е част от специалността на студента.");
            return;
        }

        try {
            StudentSubject studentSubject = new StudentSubject(student, subject, -1);
            student.enrollInSubject(studentSubject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addGrade() {
        System.out.print("Факултетен номер: ");
        String fn = Operation.scanner.nextLine().trim();
        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        System.out.print("Име на курс: ");
        String courseName = Operation.scanner.nextLine().trim();
        StudentSubject enrollment = student.findEnrollmentBySubjectName(courseName);
        if (enrollment == null) {
            System.out.println("Студентът не е записан в дисциплината \"" + courseName + "\".");
            return;
        }

        System.out.print("Оценка: ");
        try {
            float grade = Float.parseFloat(Operation.scanner.nextLine().trim());
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
        String courseName = Operation.scanner.nextLine().trim();

        List<Subject> allSubjects = Operation.g_data_store.specialties.stream()
                .flatMap(s -> s.getCourses().stream())
                .toList();

        Subject subject = Subject.findByName(allSubjects, courseName);
        if (subject == null) {
            System.out.println("Не е намерена дисциплина с име: " + courseName);
            return;
        }

        subject.printProtocol(Operation.g_data_store.students);
    }

    private static void printReport() {
        System.out.print("Факултетен номер: ");
        String fn = Operation.scanner.nextLine().trim();

        Student student = Student.findByFacultyNumber(Operation.g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        student.printAcademicReport();
    }
}
