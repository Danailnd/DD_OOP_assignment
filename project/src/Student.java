import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Представлява студент в университета.
 * <p>
 * Класът съдържа информация за студента като име, факултетен номер,
 * курс, специалност, група, статус, среден успех и записани предмети.
 * Предоставя методи за управление на записвания, оценки, академичен статус и справки.
 * </p>
 *
 * <p>Пример за използване:</p>
 * <pre>
 *     // Създаване на нов студент
 *     Student student = new Student("Иван Иванов", "12345", 2, specialty, 1, StudentStatus.ENROLLED, 4.5);
 *
 *     // Записване на студент в предмет
 *     student.enrollInSubject(new StudentSubject(student, subject));
 *
 *     // Добавяне на оценка за предмет
 *     student.addGrade(subject, 5.0f);
 *
 *     // Отпечатване на академична справка
 *     student.printAcademicReport();
 * </pre>
 *
 * @author Данаил Димитров
 */
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
    /**
     * Добавя или обновява оценка за записан предмет.
     *
     * @param subject предметът, за който се добавя оценка
     * @param grade   оценката (float)
     */
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
    /**
     * Пресмята средния успех на студента
     * на базата на всички валидни оценки.
     */
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
    /**
     * Зарежда списък с студенти от файл и списък със специалности.
     *
     * @param filePath    път към файла със студенти
     * @param specialties списък със специалности
     * @return списък със заредени студенти
     */
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
    /**
     * Записва списък със студенти във файл.
     *
     * @param students списък със студенти за записване
     * @param filePath път към файла, в който да се запишат данните
     */
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
    /**
     * Записва нов студент с подадените данни и специалност.
     *
     * @param name          име на студента
     * @param facultyNumber факултетен номер
     * @param specialtyName име на специалността
     * @param group         номер на група
     * @param specialties   списък със специалности
     * @return нов обект Student
     * @throws RuntimeException ако специалността не бъде намерена
     */
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
    /**
     * Прилага промяна в студентските данни според подадената опция и стойност.
     * <p>
     * Позволени опции са:
     * <ul>
     *   <li>"group" - промяна на група (трябва цяло число)</li>
     *   <li>"year" - промяна на курс (трябва цяло число)</li>
     *   <li>"specialty" - смяна на специалност (трябва да съществува в списъка с всички специалности)</li>
     * </ul>
     * Ако студентът е със статус "прекъснал", операцията не е разрешена.
     * При смяна на специалност се проверява дали студентът има успешно взети всички задължителни предмети
     * от минали курсове на новата специалност.
     *
     * @param option         Опцията за промяна ("group", "year", "specialty")
     * @param value          Стойност за промяна (напр. нов номер на група, курс или име на специалност)
     * @param allSpecialties Списък с всички налични специалности
     * @throws IllegalStateException    Ако студентът е със статус "прекъснал"
     * @throws IllegalArgumentException Ако подадената стойност е невалидна или специалността не е намерена
     */
    public void applyChange(String option, String value, List<Specialty> allSpecialties) {
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
                        .anyMatch(subject -> {
                            return enrolledSubjects.stream()
                                    .filter(ss -> ss.getSubject().equals(subject))
                                    .findFirst()
                                    .map(ss -> ss.getGrade() < 3.0)
                                    .orElse(true);
                        });
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
    /**
     * Прехвърля студента в следващата година (курс),
     * ако има най-много 2 неположени задължителни предмета от предходни курсове.
     *
     * @throws IllegalStateException Ако студентът има повече от 2 неположени задължителни предмета
     */
    void advanceToNextYear() {
        int currentYear = this.getCourse();
        int nextYear = currentYear + 1;

        long failedMandatoryCourses = enrolledSubjects.stream()
                .filter(ss -> ss.getSubject().isMandatory())
                .filter(ss -> ss.getSubject().getAvailableYears().stream().anyMatch(y -> y < nextYear))
                .filter(ss -> ss.getGrade() < 3.0)
                .count();

        if (failedMandatoryCourses <= 2) {
            this.setCourse(nextYear);
            System.out.println("Студентът е прехвърлен в курс " + nextYear);
        } else {
            throw new IllegalStateException("Прехвърляне не е възможно. Студентът има " + failedMandatoryCourses + " неположени задължителни предмета.");
        }
    }
    /**
     * Намира записването на студента по име на предмет.
     *
     * @param subjectName Името на предмета
     * @return Обект StudentSubject, ако е намерен, или null ако няма такова записване
     */
    public StudentSubject findEnrollmentBySubjectName(String subjectName) {
        return enrolledSubjects.stream()
                .filter(ss -> ss.getSubject().getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);
    }
    /**
     * Маркира студента като завършил, ако е положил успешно всички изпити.
     *
     * @throws RuntimeException Ако студентът има неположени изпити
     */
    void graduate() {
        boolean allPassed = enrolledSubjects.stream()
                .allMatch(ss -> ss.getGrade() >= 3.0);

        if (!allPassed) {
            throw new RuntimeException("Студентът не е положил успешно всички изпити.");
        }

        this.setStatus(StudentStatus.GRADUATED);
    }
    /**
     * Отбелязва студента като прекъснал обучението си.
     *
     * @throws RuntimeException Ако студентът вече е със статус "прекъснал" или е завършил
     */
    void interrupt() {
        if (this.getStatus() == StudentStatus.SUSPENDED) {
            throw new RuntimeException("Студентът вече е отбелязан като прекъснал.");
        }

        if (this.getStatus() == StudentStatus.GRADUATED) {
            throw new RuntimeException("Този студент вече е завършил.");
        }

        this.setStatus(StudentStatus.SUSPENDED);
    }
    /**
     * Възстановява студента със статус "записан",
     * ако в момента е със статус "прекъснал".
     *
     * @throws RuntimeException Ако студентът вече е записан или е завършил
     */
    void resume() {
        if (this.getStatus() == StudentStatus.ENROLLED) {
            throw new RuntimeException("Студентът вече е отбелязан като записан.");
        }

        if (this.getStatus() == StudentStatus.GRADUATED) {
            throw new RuntimeException("Този студент вече е завършил.");
        }

        this.setStatus(StudentStatus.ENROLLED);
    }
    /**
     * Извежда информация за студента в конзолата:
     * име, факултетен номер, специалност, курс, група, статус и среден успех.
     */
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
    /**
     * Търси студент в списък по факултетен номер.
     *
     * @param students      Списък със студенти
     * @param facultyNumber Факултетен номер за търсене
     * @return Намереният студент или null, ако не съществува
     */
    public static Student findByFacultyNumber(List<Student> students, String facultyNumber) {
        return students.stream()
                .filter(s -> s.getFacultyNumber().equalsIgnoreCase(facultyNumber))
                .findFirst()
                .orElse(null);
    }
    public void loadEnrollment(StudentSubject studentSubject) {
        enrolledSubjects.add(studentSubject);
    }
    /**
     * Записва студента в даден предмет.
     *
     * @param studentSubject обект, описващ записването на студента в предмет
     * @throws IllegalStateException    ако студентът е прекъснал обучението или вече е записан
     * @throws IllegalArgumentException ако предметът не е част от специалността или не е достъпен за курса
     */
    public void enrollInSubject(StudentSubject studentSubject) {
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
    }

    /**
     * Отпечатва академична справка с взетите и неизпълнените предмети,
     * както и средния успех.
     */
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
    public List<StudentSubject> getEnrolledSubjects() {
        return enrolledSubjects;
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
