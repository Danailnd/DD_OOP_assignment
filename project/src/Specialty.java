import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Specialty {
    private UUID id;
    private String name;
    private List<Subject> courses;

    public Specialty() {
    }

    public Specialty(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void addCourse(Subject course) {
        courses.add(course);
    }
    public List<Subject> getCoursesForYear(int year) {
        List<Subject> result = new ArrayList<>();
        for (Subject course : courses) {
            if (course.isAvailableForYear(year)) {
                result.add(course);
            }
        }
        return result;
    }

//  Getters
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public List<Subject> getCourses() {
        return courses;
    }

//    Setters
    public void setId(UUID id){this.id = id;}
    public void setName(String name) {
        this.name = name;
    }
}
