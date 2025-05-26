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

    public static List<Specialty> loadFromUserInput(String filePath) {
        List<Specialty> loadedSpecialities = loadFromFile(filePath);
        if (loadedSpecialities == null) {
            System.out.println("Неуспешно зареждане на специалности.");
        } else {
            System.out.println("Успешно заредени " + loadedSpecialities.size() + " специалности.");
        }
        return loadedSpecialities;
    }

    public static List<Specialty> loadFromFile(String path) {
        return JsonDeserializeHelper.loadSpecialtiesFromFile(path);
    }

    static void saveToFile(List<Specialty> specialties, String filePath) {
        List<SpecialtyDTO> dtoList = specialties.stream()
                .map(specialty -> {
                    SpecialtyDTO dto = new SpecialtyDTO();
                    dto.id = specialty.getId().toString();
                    dto.name = specialty.getName();
                    dto.courses = specialty.getCourses();
                    return dto;
                }).toList();

        boolean success = JsonSerializeHelper.saveToFile(dtoList, filePath);
        if (!success) {
            throw new RuntimeException("Грешка при записване на специалностите във файла: " + filePath);
        }
    }

    public Subject findSubjectByName(String subjectName) {
        return courses.stream()
                .filter(s -> s.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);
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
