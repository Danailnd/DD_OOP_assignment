import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Представлява специалност в университета.
 * <p>
 * Класът съдържа информация за специалността като уникален идентификатор, име
 * и списък от дисциплини (курсове), които се изучават в рамките на специалността.
 * Предоставя методи за добавяне на дисциплини, извличане на дисциплини за конкретен курс,
 * както и за зареждане и записване на специалности от и във файлове.
 * </p>
 *
 * <p>Пример за използване:</p>
 * <pre>
 *     // Създаване на нова специалност
 *     Specialty specialty = new Specialty("Компютърни науки");
 *
 *     // Добавяне на курс към специалността
 *     specialty.addCourse(new Subject("Програмиране", true, List.of(1, 2)));
 *
 *     // Получаване на курсовете за първи курс
 *     List&lt;Subject&gt; firstYearCourses = specialty.getCoursesForYear(1);
 *
 *     // Зареждане на специалности от файл
 *     List&lt;Specialty&gt; allSpecialties = Specialty.loadFromUserInput("specialties.json");
 *
 *     // Записване на специалности във файл
 *     Specialty.saveToFile(allSpecialties, "output.json");
 * </pre>
 *
 * @author Данаил Димитров
 */
public class Specialty {
    private UUID id;
    private String name;
    private List<Subject> courses;

    public Specialty() {
    }
    /**
     * Конструктор, който създава специалност с подадено име и празен списък с курсове.
     *
     * @param name Името на специалността.
     */
    public Specialty(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.courses = new ArrayList<>();
    }
    /**
     * Добавя дисциплина (курс) към списъка с курсове на специалността.
     *
     * @param course Дисциплината, която се добавя.
     */
    public void addCourse(Subject course) {
        courses.add(course);
    }
    /**
     * Връща списък с дисциплини, които са налични за дадена учебна година.
     *
     * @param year Годината, за която се извличат курсовете.
     * @return Списък с курсове, които са достъпни за посочената година.
     */
    public List<Subject> getCoursesForYear(int year) {
        List<Subject> result = new ArrayList<>();
        for (Subject course : courses) {
            if (course.isAvailableForYear(year)) {
                result.add(course);
            }
        }
        return result;
    }

    /**
     * Търси дисциплина по име сред курсовете на специалността.
     *
     * @param subjectName Името на дисциплината.
     * @return Обект Subject, ако е намерен, или null ако няма такъв курс.
     */
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
