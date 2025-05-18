import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Student {

    private final UUID id;
    private String name;
    private String facultyNumber;
    private int course;
    private Specialty specialty;
    private int group;
    private StudentStatus status;
    private double averageGrade;

    private final List<StudentSubject> enrolledSubjects = new ArrayList<>();

    public Student() {
        this.id = UUID.randomUUID();
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

    public void enrollInSubject(Subject subject) {
        for (StudentSubject ss : enrolledSubjects) {
            if (ss.getSubject().equals(subject)) {
                System.out.println("Already enrolled in: " + subject.getName());
                return;
            }
        }
        enrolledSubjects.add(new StudentSubject(this, subject, -1));
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

    public List<StudentSubject> getPassedExams() {
        List<StudentSubject> result = new ArrayList<>();
        for (StudentSubject ss : enrolledSubjects) {
            if (ss.getGrade() >= 3.0) {
                result.add(ss);
            }
        }
        return result;
    }

    public List<StudentSubject> getFailedOrNotTaken() {
        List<StudentSubject> result = new ArrayList<>();
        for (StudentSubject ss : enrolledSubjects) {
            if (ss.getGrade() < 3.0) {
                result.add(ss);
            }
        }
        return result;
    }

    private void recalculateAverage() {
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

    public void printAcademicTranscript() {
        System.out.println("Академична справка за " + name + " :");
        for (StudentSubject ss : enrolledSubjects) {
            String gradeText = (ss.getGrade() >= 0) ? String.format("%.2f", ss.getGrade()) : "Не е взет";
            System.out.println("- " + ss.getSubject().getName() + ": " + gradeText);
        }

        System.out.println("Среден успех: " + String.format("%.2f", averageGrade));
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
