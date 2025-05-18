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
