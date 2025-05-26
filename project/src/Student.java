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

    public void enrollInSubject(StudentSubject studentSubject) {
        for (StudentSubject ss : enrolledSubjects) {
            if (ss.equals(studentSubject)) {
                System.out.println("Already enrolled in: " + studentSubject.getStudent().getName());
                return;
            }
        }
        enrolledSubjects.add(studentSubject);
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
