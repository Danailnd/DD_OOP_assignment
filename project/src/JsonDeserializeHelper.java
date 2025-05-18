import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;

public class JsonDeserializeHelper {

    private static final Gson gson = new Gson();

    public static List<Specialty> loadSpecialtiesFromFile(String path) {
        try (FileReader reader = new FileReader(path)) {
            Type listType = new TypeToken<List<Specialty>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            System.out.println("Грешка при зареждане на файл: " + e.getMessage());
            return null;
        }
    }

    public static List<Student> loadStudentsFromFile(String filePath, List<Specialty> specialties) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<StudentDTO> dtos = gson.fromJson(reader, new TypeToken<List<StudentDTO>>() {}.getType());

            List<Student> result = new ArrayList<>();
            for (StudentDTO dto : dtos) {
                Specialty matched = specialties.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(dto.specialtyName))
                        .findFirst()
                        .orElse(null);

                if (matched == null) {
                    System.out.println("Специалността не е намерена: " + dto.specialtyName);
                    continue;
                }

                Student student = new Student();
                student.setName(dto.name);
                student.setFacultyNumber(dto.facultyNumber);
                student.setCourse(dto.course);
                student.setGroup(dto.group);
                student.setStatus(StudentStatus.valueOf(dto.status)); // Make sure case matches!
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

}
