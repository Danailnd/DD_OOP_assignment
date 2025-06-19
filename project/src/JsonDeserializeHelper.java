import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Помощен клас за десериализация на данни от JSON файлове.
 * <p>
 * Този клас предоставя методи за зареждане на специалности, студенти и студентски предмети
 * от JSON файлове във вътрешните структури на приложението.
 * </p>
 *
 * <p>Методите използват библиотеката Gson за конвертиране на JSON към Java обекти.</p>
 *
 * @author Данаил Димитров
 */
public class JsonDeserializeHelper {

    private static final Gson gson = new Gson();

    /**
     * Зарежда списък от специалности от JSON файл.
     *
     * @param path Път до JSON файла със специалности.
     * @return Списък със специалности, или {@code null} при грешка.
     */
    public static List<Specialty> loadSpecialtiesFromFile(String path) {
        try (FileReader reader = new FileReader(path)) {
            Type listType = new TypeToken<List<Specialty>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            System.out.println("Грешка при зареждане на файл: " + e.getMessage());
            return null;
        }
    }

    /**
     * Зарежда списък от студенти от JSON файл, като свързва всеки студент със съответната специалност.
     *
     * @param filePath Път до JSON файла със студенти.
     * @param specialties Списък със специалности, използван за свързване.
     * @return Списък със студенти, или {@code null} при грешка.
     */
    public static List<Student> loadStudentsFromFile(String filePath, List<Specialty> specialties) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<StudentDTO> dtos = gson.fromJson(reader, new TypeToken<List<StudentDTO>>() {}.getType());

            List<Student> result = new ArrayList<>();
            for (StudentDTO dto : dtos) {
                Specialty matched = specialties.stream()
                        .filter(s -> s.getId().toString().equals(dto.specialtyId))
                        .findFirst()
                        .orElse(null);

                if (matched == null) {
                    System.out.println("Специалността с ID не е намерена: " + dto.specialtyId);
                    continue;
                }

                Student student = new Student();
                student.setId(UUID.fromString(dto.id));
                student.setName(dto.name);
                student.setFacultyNumber(dto.facultyNumber);
                student.setCourse(dto.course);
                student.setGroup(dto.group);
                student.setStatus(StudentStatus.valueOf(dto.status));
                student.setAverageGrade(dto.averageGrade);
                student.setSpecialty(matched);

                result.add(student);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Грешка при зареждане на студенти: " + e.getMessage());
            return null;
        }
    }
    /**
     * Зарежда списък от студентски предмети (StudentSubject) от JSON файл, като свързва студентите и предметите.
     *
     * @param filePath Път до JSON файла със студентски предмети.
     * @param students Списък със студенти, използван за свързване.
     * @param specialties Списък със специалности, използван за свързване на предмети.
     * @return Списък със студентски предмети, или {@code null} при грешка.
     */
    public static List<StudentSubject> loadStudentSubjectsFromFile(
            String filePath,
            List<Student> students,
            List<Specialty> specialties
    ) {
        try (Reader reader = new FileReader(filePath)) {
            List<StudentSubjectDTO> dtos = gson.fromJson(reader, new TypeToken<List<StudentSubjectDTO>>() {}.getType());
            List<StudentSubject> result = new ArrayList<>();

            for (StudentSubjectDTO dto : dtos) {
                UUID studentId = UUID.fromString(dto.studentId);
                UUID subjectId = UUID.fromString(dto.subjectId);

                Student student = students.stream()
                        .filter(s -> s.getId().equals(studentId))
                        .findFirst()
                        .orElse(null);

                if (student == null) {
                    System.out.println("Студент с ID " + dto.studentId + " не е намерен.");
                    continue;
                }

                Subject subject = specialties.stream()
                        .flatMap(s -> s.getCourses().stream())
                        .filter(sub -> sub.getId().equals(subjectId))
                        .findFirst()
                        .orElse(null);

                if (subject == null) {
                    System.out.println("Предмет с ID " + dto.subjectId + " не е намерен.");
                    continue;
                }

                StudentSubject ss = new StudentSubject(student, subject, dto.grade);
                student.loadEnrollment(ss);
                student.addGrade(subject, dto.grade);

                result.add(ss);
            }

            return result;
        } catch (Exception e) {
            System.out.println("Грешка при зареждане на StudentSubjects: " + e.getMessage());
            return null;
        }
    }
}
