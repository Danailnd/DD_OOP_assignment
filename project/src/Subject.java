import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Представлява дисциплина (предмет) в учебната програма на университета.
 * <p>
 * Класът съдържа информация за дисциплината като уникален идентификатор, име,
 * дали е задължителна и за кои години от обучението е налична.
 * Предоставя функционалности за проверка на достъпност на дисциплината по години,
 * както и за генериране на протокол с информация за студентите, записани по дисциплината.
 * </p>
 *
 * <p>Пример за използване:</p>
 * <pre>
 *     // Създаване на нов предмет
 *     Subject math = new Subject("Математика", true, List.of(1, 2, 3));
 *
 *     // Проверка дали предметът е наличен за 2-ри курс
 *     boolean available = math.isAvailableForYear(2);
 *
 *     // Отпечатване на протокол за дисциплината с всички записани студенти
 *     math.printProtocol(allStudents);
 * </pre>
 *
 * @author Данаил Димитров
 */
public class Subject {
    private UUID id;
    private String name;
    private boolean isMandatory;
    private List<Integer> availableYears;

    public Subject() {
    }

    /**
     * Конструктор, който създава нов предмет с посочени параметри.
     * @param name Името на дисциплината.
     * @param isMandatory Дали дисциплината е задължителна.
     * @param availableYears Списък с години, в които дисциплината се изучава.
     */
    public Subject(String name, boolean isMandatory, List<Integer> availableYears) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.isMandatory = isMandatory;
        this.availableYears = availableYears;
    }
    /**
     * Проверява дали дисциплината е налична за дадена година на обучение.
     * @param year Година на обучение.
     * @return true ако дисциплината е налична за тази година, false в противен случай.
     */
    public boolean isAvailableForYear(int year) {
        return availableYears.contains(year);
    }
    /**
     * Печата протокол с информация за всички студенти, записани по тази дисциплина.
     * Протоколът е групиран по специалност и курс, като за всеки студент се извежда
     * факултетният номер, име, група и оценка.
     * @param allStudents Списък с всички студенти.
     */
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
    /**
     * Търси дисциплина по нейното име в даден списък.
     * @param allSubjects Списък с всички дисциплини.
     * @param name Името на дисциплината за търсене.
     * @return Обект Subject, ако е намерен, или null ако няма такъв.
     */
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
