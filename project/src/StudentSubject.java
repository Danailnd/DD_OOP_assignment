import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Представлява връзка между студент и дисциплина (предмет),
 * съдържаща оценка за съответния студентски предмет.
 * <p>
 * Класът съдържа информация за студента, дисциплината и получената оценка.
 * Предоставя методи за зареждане и записване от/в JSON файлове, както и задаване на оценки.
 * </p>
 *
 * <p>Пример за използване:</p>
 * <pre>
 *     StudentSubject enrollment = new StudentSubject(student, subject, 0.0f);
 *     enrollment.assignGrade(5.0f);
 *     enrollment.saveToFile(listOfEnrollments, "enrollments.json");
 * </pre>
 *
 * @author Данаил Димитров
 */
public class StudentSubject {
    private final UUID id;
    private Student student;
    private Subject subject;
    private float grade;

    /**
     * Създава нов запис за студентски предмет с даден студент, предмет и оценка.
     *
     * @param student Студентът, който е записан.
     * @param subject Предметът, в който е записан студентът.
     * @param grade Оценката на студента по този предмет.
     */
    public StudentSubject(Student student, Subject subject, float grade){
        this.id = UUID.randomUUID();
        this.student = student;
        this.subject = subject;
        this.grade = grade;
    }
    /**
     * Зарежда студентски предмети от потребителски вход (файл).
     * Отпечатва резултат от операцията.
     *
     * @param filePath Път към файла за зареждане.
     * @param students Списък от студенти, към които да се свържат записите.
     * @param specialties Списък от специалности за проверка.
     */
    public static void loadFromUserInput(String filePath, List<Student> students, List<Specialty> specialties) {
        List<StudentSubject> loaded = loadFromFile(filePath, students, specialties);
        if (loaded == null) {
            System.out.println("Неуспешно зареждане на студентски предмети.");
        } else {
            System.out.println("Успешно заредени " + loaded.size() + " студенти.");
        }
    }
    /**
     * Зарежда студентски предмети от JSON файл.
     *
     * @param path Път към JSON файла.
     * @param students Списък от студенти за свързване на записите.
     * @param specialties Списък от специалности за валидиране.
     * @return Списък със студентски предмети или null при неуспех.
     */
    public static List<StudentSubject> loadFromFile(String path, List<Student> students, List<Specialty> specialties) {
        return JsonDeserializeHelper.loadStudentSubjectsFromFile(path, students, specialties);
    }

    /**
     * Записва списък със студентски предмети във файл.
     *
     * @param subjects Списък със студентски предмети за запис.
     * @param filePath Път към файла за запис.
     * @throws RuntimeException Ако възникне грешка при записването.
     */
    static void saveToFile(List<StudentSubject> subjects, String filePath) throws IOException {
        List<StudentSubjectDTO> dtoList = subjects.stream()
                .map(ss -> {
                    StudentSubjectDTO dto = new StudentSubjectDTO();
                    dto.studentId = ss.getStudent().getId().toString();
                    dto.subjectId = ss.getSubject().getId().toString();
                    dto.grade = ss.getGrade();
                    return dto;
                }).toList();

        boolean success = JsonSerializeHelper.saveToFile(dtoList, filePath);
        if (!success) {
            throw new RuntimeException("Грешка при записване на студентските предмети във файла: " + filePath);
        }
    }
    /**
     * Задава оценка за студентския предмет.
     *
     * @param grade Оценка, която трябва да е между 2.0 и 6.0.
     * @throws IllegalArgumentException ако оценката е извън допустимия диапазон.
     * @throws IllegalStateException ако студентът е със статус "прекъснал" (SUSPENDED).
     */
    public void assignGrade(float grade) {
        if (grade < 2.0 || grade > 6.0) {
            throw new IllegalArgumentException("Оценката трябва да е между 2.00 и 6.00.");
        }

        if (student.getStatus() == StudentStatus.SUSPENDED) {
            throw new IllegalStateException("Студентът е прекъснал и не може да се явява на изпити.");
        }

        this.setGrade(grade);
        this.student.recalculateAverage();
    }
    // Getters
    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public float getGrade() {
        return grade;
    }

    // Setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
