import java.util.List;
import java.util.UUID;

public class StudentSubject {
    private final UUID id;
    private Student student;
    private Subject subject;
    private float grade;

    public StudentSubject(Student student, Subject subject, float grade){
        this.id = UUID.randomUUID();
        this.student = student;
        this.subject = subject;
        this.grade = grade;
    }
    public static List<StudentSubject> loadFromUserInput(String filePath, List<Student> students, List<Specialty> specialties) {
        List<StudentSubject> loaded = loadFromFile(filePath, students, specialties);
        if (loaded == null) {
            System.out.println("Неуспешно зареждане на студентски предмети.");
        } else {
            System.out.println("Успешно заредени " + loaded.size() + " студенти.");
        }
        return loaded;
    }
    public static List<StudentSubject> loadFromFile(String path, List<Student> students, List<Specialty> specialties) {
        return JsonDeserializeHelper.loadStudentSubjectsFromFile(path, students, specialties);
    }
    static void saveToFile(List<StudentSubject> subjects, String filePath) {
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
