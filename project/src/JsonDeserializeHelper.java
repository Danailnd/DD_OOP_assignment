import com.google.gson.Gson;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

public class JsonDeserializeHelper {

    private static final Gson gson = new Gson();

    public static DataStore loadFromCombinedFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            UniversityDTO bundle = gson.fromJson(reader, UniversityDTO.class);

            if (bundle.specialties == null || bundle.students == null || bundle.studentSubjects == null) {
                System.out.println("Грешка: Липсват части от данните във файла.");
                return null;
            }

            for (Student student : bundle.students) {
                UUID specialtyId = student.getSpecialty().getId();

                Specialty matched = bundle.specialties.stream()
                        .filter(s -> s.getId().equals(specialtyId))
                        .findFirst()
                        .orElse(null);

                if (matched == null) {
                    System.out.println("Специалността с ID не е намерена: " + specialtyId);
                    continue;
                }

                student.setSpecialty(matched);
            }

            for (StudentSubject ss : bundle.studentSubjects) {
                UUID studentId = ss.getStudent().getId();
                UUID subjectId = ss.getSubject().getId();

                Student student = bundle.students.stream()
                        .filter(s -> s.getId().equals(studentId))
                        .findFirst()
                        .orElse(null);

                if (student == null) {
                    System.out.println("Студент с ID " + studentId + " не е намерен.");
                    continue;
                }

                Subject subject = bundle.specialties.stream()
                        .flatMap(s -> s.getCourses().stream())
                        .filter(sub -> sub.getId().equals(subjectId))
                        .findFirst()
                        .orElse(null);

                if (subject == null) {
                    System.out.println("Предмет с ID " + subjectId + " не е намерен.");
                    continue;
                }

                ss.setStudent(student);
                ss.setSubject(subject);
                student.loadEnrollment(ss);
                student.addGrade(subject, ss.getGrade());
            }

            DataStore store = new DataStore();
            store.specialties = bundle.specialties;
            store.students = bundle.students;
            store.studentSubjects = bundle.studentSubjects;
            store.filePath = filePath;

            return store;

        } catch (Exception e) {
            System.out.println("Грешка при зареждане на файл: " + e.getMessage());
            return null;
        }
    }
}
