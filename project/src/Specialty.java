import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Specialty {
    private final UUID id;

    private String name;
    private final List<Course> courses;

    public Specialty(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }
    public List<Course> getCoursesForYear(int year) {
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
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

    public List<Course> getCourses() {
        return courses;
    }

//    Setters
    public void setName(String name) {
        this.name = name;
    }
}
