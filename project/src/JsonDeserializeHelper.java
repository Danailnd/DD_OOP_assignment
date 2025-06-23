import com.google.gson.Gson;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonDeserializeHelper {

    private static final Gson gson = new Gson();

    public static DataStore loadFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            UniversityDTO bundle = gson.fromJson(reader, UniversityDTO.class);

            if (bundle.specialties == null || bundle.students == null || bundle.studentSubjects == null) {
                System.out.println("Грешка: Липсват части от данните във файла.");
                return null;
            }

            // Convert StudentDTO → Student
            List<Student> students = new ArrayList<>();
            for (StudentDTO dto : bundle.students) {
                Specialty matched = bundle.specialties.stream()
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
                students.add(student);
            }

            List<StudentSubject> studentSubjects = new ArrayList<>();
            for (StudentSubjectDTO dto : bundle.studentSubjects) {
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

                Subject subject = bundle.specialties.stream()
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
                studentSubjects.add(ss);
            }

            DataStore store = new DataStore();
            store.specialties = bundle.specialties;
            store.students = students;
            store.studentSubjects = studentSubjects;
            store.filePath = filePath;

            return store;

        } catch (Exception e) {
            System.out.println("Грешка при зареждане на файл: " + e.getMessage());
            return null;
        }
    }
}
