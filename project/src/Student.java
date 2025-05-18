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
