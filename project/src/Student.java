import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Student {

    private  UUID id;
    private String name;
    private String facultyNumber;
    private int course;
    private Specialty specialty;
    private int group;
    private StudentStatus status;
    private double averageGrade;
    private final List<StudentSubject> enrolledSubjects = new ArrayList<>();

    public Student() {

    }
    public Student( String name, String facultyNumber, int course, Specialty specialty, int group, StudentStatus status, double averageGrade) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.course = course;
        this.specialty = specialty;
        this.group = group;
        this.status = status;
        this.averageGrade = averageGrade;
    }
    public void addGrade(Subject subject, float grade) {
        for (StudentSubject ss : enrolledSubjects) {
            if (ss.getSubject().equals(subject)) {
                ss.setGrade(grade);
                recalculateAverage();
                return;
            }
        }
        System.out.println("Student is not enrolled in the subject: " + subject.getName());
    }
    public void recalculateAverage() {
        if (enrolledSubjects.isEmpty()) {
            this.averageGrade = 0;
            return;
        }

        double total = 0;
        int count = 0;

        for (StudentSubject ss : enrolledSubjects) {
            float g = ss.getGrade();
            if (g >= 0) {
                total += g;
                count++;
            }
        }

        this.averageGrade = count == 0 ? 0 : total / count;
    }
    public static List<Student> loadFromUserInput(String filePath, List<Specialty> specialties) {
        List<Student> loadedStudents = loadFromFile(filePath, specialties);
        if (loadedStudents == null) {
            System.out.println("Неуспешно зареждане на студенти.");
        } else {
            System.out.println("Успешно заредени " + loadedStudents.size() + " студенти.");
        }
        return loadedStudents;
    }
    public static List<Student> loadFromFile(String path, List<Specialty> specialties) {
        return JsonDeserializeHelper.loadStudentsFromFile(path, specialties);
    }
    static void saveToFile(List<Student> students, String filePath) {
        List<StudentDTO> studentDTOs = students.stream()
                .map(student -> {
                    StudentDTO dto = new StudentDTO();
                    dto.id = student.getId().toString();
                    dto.name = student.getName();
                    dto.facultyNumber = student.getFacultyNumber();
                    dto.course = student.getCourse();
                    dto.specialtyId = student.getSpecialty().getId().toString();
                    dto.group = student.getGroup();
                    dto.status = student.getStatus().toString();
                    dto.averageGrade = student.getAverageGrade();
                    return dto;
                }).toList();

        boolean success = JsonSerializeHelper.saveToFile(studentDTOs, filePath);
        if (!success) {
            throw new RuntimeException("Грешка при записване на студентите във файла: " + filePath);
        }
    }
    public static Student enroll(
            String name,
            String facultyNumber,
            String specialtyName,
            int group,
            List<Specialty> specialties
    ) {
        Specialty foundSpecialty = specialties.stream()
                .filter(s -> s.getName().equalsIgnoreCase(specialtyName))
                .findFirst()
                .orElse(null);

        if (foundSpecialty == null) {
            throw new RuntimeException("Специалност с име \"" + specialtyName + "\" не е намерена.");
        }

        Student newStudent = new Student();
        newStudent.setId(java.util.UUID.randomUUID());
        newStudent.setName(name);
        newStudent.setFacultyNumber(facultyNumber);
        newStudent.setSpecialty(foundSpecialty);
        newStudent.setCourse(1);
        newStudent.setGroup(group);
        newStudent.setStatus(StudentStatus.ENROLLED);
        newStudent.setAverageGrade(0.0);

        return newStudent;
    }

    public void applyChange(String option, String value, List<Specialty> allSpecialties, List<StudentSubject> allStudentSubjects) {
        if (this.getStatus() == StudentStatus.SUSPENDED) {
            throw new IllegalStateException("Операцията не е позволена. Студентът е със статус 'прекъснал'.");
        }

        switch (option.toLowerCase()) {
            case "group":
                try {
                    int newGroup = Integer.parseInt(value);
                    this.setGroup(newGroup);
                    System.out.println("Студентът е прехвърлен в група " + newGroup);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Невалиден номер на група.");
                }
                break;

            case "year":
                try {
                    int newYear = Integer.parseInt(value);
                    this.setCourse(newYear);
                    System.out.println("Курсът е обновен на " + newYear);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Невалидна стойност за курс.");
                }
                break;

            case "specialty":
                Specialty newSpecialty = allSpecialties.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(value))
                        .findFirst()
                        .orElse(null);

                if (newSpecialty == null) {
                    throw new IllegalArgumentException("Специалност с име \"" + value + "\" не е намерена.");
                }

                int studentYear = this.getCourse();

                List<Subject> requiredSubjects = newSpecialty.getCourses().stream()
                        .filter(s -> s.isMandatory() &&
                                s.getAvailableYears().stream().anyMatch(y -> y < studentYear))
                        .toList();

                boolean isFailingRequiredClass = requiredSubjects.stream()
                        .anyMatch(subject -> allStudentSubjects.stream()
                                .anyMatch(ss ->
                                        ss.getStudent().equals(this) &&
                                                ss.getSubject().equals(subject) &&
                                                ss.getGrade() < 3.0));

                if (!isFailingRequiredClass) {
                    this.setSpecialty(newSpecialty);
                    System.out.println("Студентът е прехвърлен в специалност " + newSpecialty.getName());
                } else {
                    System.out.println("Прехвърляне не е възможно. Студентът няма положени всички задължителни предмети от минали курсове на новата специалност.");
                }
                break;

            default:
                throw new IllegalArgumentException("Невалидна опция. Избери една от следните: specialty, group, year.");
        }
    }

    void advanceToNextYear(int targetYear, List<StudentSubject> allStudentSubjects) {
        int currentYear = this.getCourse();

        if (targetYear != currentYear + 1) {
            throw new IllegalArgumentException("Прехвърляне е разрешено само към следващ курс.");
        }

        long failedMandatoryCourses = allStudentSubjects.stream()
                .filter(ss -> ss.getStudent().equals(this))
                .filter(ss -> ss.getSubject().isMandatory())
                .filter(ss -> ss.getSubject().getAvailableYears().stream().anyMatch(y -> y < targetYear))
                .filter(ss -> ss.getGrade() < 3.0)
                .count();

        if (failedMandatoryCourses <= 2) {
            this.setCourse(targetYear);
            System.out.println("Студентът е прехвърлен в курс " + targetYear);
        } else {
            throw new IllegalStateException("Прехвърляне не е възможно. Студентът има " + failedMandatoryCourses + " неположени задължителни предмета.");
        }
    }
    void graduate(List<StudentSubject> allSubjects) {
        List<StudentSubject> enrolledSubjects = allSubjects.stream()
                .filter(ss -> ss.getStudent().equals(this))
                .toList();

        boolean allPassed = enrolledSubjects.stream()
                .allMatch(ss -> ss.getGrade() >= 3.0);

        if (!allPassed) {
            throw new RuntimeException("Студентът не е положил успешно всички изпити.");
        }

        this.setStatus(StudentStatus.GRADUATED);
    }

    void interrupt() {
        if (this.getStatus() == StudentStatus.SUSPENDED) {
            throw new RuntimeException("Студентът вече е отбелязан като прекъснал.");
        }

        if (this.getStatus() == StudentStatus.GRADUATED) {
            throw new RuntimeException("Този студент вече е завършил.");
        }

        this.setStatus(StudentStatus.SUSPENDED);
    }
    void resume() {
        if (this.getStatus() == StudentStatus.ENROLLED) {
            throw new RuntimeException("Студентът вече е отбелязан като записан.");
        }

        if (this.getStatus() == StudentStatus.GRADUATED) {
            throw new RuntimeException("Този студент вече е завършил.");
        }

        this.setStatus(StudentStatus.ENROLLED);
    }
    void displayInfo() {
        System.out.printf("- Име: %s | Факултетен номер: %s | Специалност: %s | Курс: %d | Група: %d | Статус: %s | Среден успех: %.2f%n",
                this.getName(),
                this.getFacultyNumber(),
                (this.getSpecialty() != null ? this.getSpecialty().getName() : "Неизвестна"),
                this.getCourse(),
                this.getGroup(),
                this.getStatus(),
                this.getAverageGrade()
        );
    }
    public static Student findByFacultyNumber(List<Student> students, String facultyNumber) {
        return students.stream()
                .filter(s -> s.getFacultyNumber().equalsIgnoreCase(facultyNumber))
                .findFirst()
                .orElse(null);
    }
    public void loadEnrollment(StudentSubject studentSubject) {
        enrolledSubjects.add(studentSubject);
    }
    public StudentSubject enrollInSubject(StudentSubject studentSubject) {
        if (status == StudentStatus.SUSPENDED) {
            throw new IllegalStateException("Студентът е с прекъснато обучение и не може да записва дисциплини.");
        }

        if (specialty == null) {
            throw new IllegalStateException("Студентът няма зададена специалност.");
        }

        Subject subject = studentSubject.getSubject();

        if (!specialty.getCourses().contains(subject)) {
            throw new IllegalArgumentException("Дисциплината не е част от специалността на студента.");
        }

        if (!subject.getAvailableYears().contains(course)) {
            throw new IllegalArgumentException("Дисциплината не се предлага за курс " + course + ".");
        }

        boolean alreadyEnrolled = enrolledSubjects.stream()
                .anyMatch(ss -> ss.getSubject().equals(subject));

        if (alreadyEnrolled) {
            throw new IllegalStateException("Студентът вече е записан в тази дисциплина.");
        }

        enrolledSubjects.add(studentSubject);
        System.out.println("Успешно записване на дисциплината: " + subject.getName());
        return studentSubject;
    }
    public void printAcademicReport() {
        if (enrolledSubjects.isEmpty()) {
            System.out.println("Няма записани дисциплини за този студент.");
            return;
        }

        List<StudentSubject> passed = new ArrayList<>();
        List<StudentSubject> missing = new ArrayList<>();

        for (StudentSubject ss : enrolledSubjects) {
            if (ss.getGrade() >= 3.0) {
                passed.add(ss);
            } else {
                missing.add(ss);
            }
        }

        System.out.println("\n--- Академична справка за " + name + " (ФН: " + facultyNumber + ") ---");

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

        System.out.printf("%n Среден успех: %.2f%n", averageGrade);
    }

    //    getters and setters
    public UUID getId(){
       return id;
    }
    public String getName() {
        return name;
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public int getCourse() {
        return course;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public int getGroup() {
        return group;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
