import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//Дисциплина
public class Subject {
    private UUID id;
    private String name;
    private boolean isMandatory;
    private List<Integer> availableYears;

    public Subject() {
    }
    public Subject(String name, boolean isMandatory, List<Integer> availableYears) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.isMandatory = isMandatory;
        this.availableYears = availableYears;
    }
    public boolean isAvailableForYear(int year) {
        return availableYears.contains(year);
    }
    public void printProtocol(List<Student> allStudents) {
        // Collect all StudentSubject enrollments for this subject from all students
        List<StudentSubject> enrolledInCourse = allStudents.stream()
                .flatMap(student -> student.getEnrolledSubjects().stream())
                .filter(ss -> ss.getSubject().equals(this))
                .toList();

        if (enrolledInCourse.isEmpty()) {
            System.out.println("Няма записани студенти в дисциплината \"" + this.getName() + "\".");
            return;
        }

        Map<String, Map<Integer, List<StudentSubject>>> grouped =
                enrolledInCourse.stream()
                        .collect(Collectors.groupingBy(
                                ss -> ss.getStudent().getSpecialty().getName(),
                                Collectors.groupingBy(ss -> ss.getStudent().getCourse())
                        ));

        System.out.println("\n--- Протокол за дисциплина: " + this.getName() + " ---");

        for (String specialty : grouped.keySet()) {
            Map<Integer, List<StudentSubject>> byYear = grouped.get(specialty);
            for (Integer year : byYear.keySet()) {
                List<StudentSubject> list = byYear.get(year).stream()
                        .sorted(Comparator.comparing(ss -> ss.getStudent().getFacultyNumber()))
                        .toList();

                for (StudentSubject ss : list) {
                    Student s = ss.getStudent();
                    String gradeStr = ss.getGrade() > 0 ? String.format("%.2f", ss.getGrade()) : "Няма оценка";
                    System.out.printf("ФН: %s | Име: %s | Група: %d | Оценка: %s%n",
                            s.getFacultyNumber(), s.getName(), s.getGroup(), gradeStr);
                }
            }
        }
    }

    public static Subject findByName(List<Subject> allSubjects, String name) {
        if (allSubjects == null || name == null) return null;

        return allSubjects.stream()
                .filter(subject -> subject.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    // Getters
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isMandatory() {
        return isMandatory;
    }
    public List<Integer> getAvailableYears() {
        return availableYears;
    }

    // Setters
    public void setId(UUID id){this.id = id;}
    public void setName(String name) {
        this.name = name;
    }
    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }
    public void setAvailableYears(List<Integer> availableYears) {
        this.availableYears = availableYears;
    }
}
