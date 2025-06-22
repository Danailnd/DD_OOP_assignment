import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Помощен клас за сериализация на данни в JSON формат и записването им във файлове.
 * <p>
 * Използва библиотеката Gson за конвертиране на Java обекти в JSON с форматиран (pretty print) изход.
 * </p>
 *
 * <p>Методите позволяват запис на цялостна структура от данни в един JSON файл.</p>
 */
public class JsonSerializeHelper {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static boolean saveToFile(DataStore store, String filePath) {
        if (filePath == null || filePath.isBlank()) {
            System.out.println("Невалиден път до файл.");
            return false;
        }

        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            System.out.println("Директорията не съществува: " + parent.getAbsolutePath());
            return false;
        }
        UniversityDTO dto = new UniversityDTO();
        dto.specialties = store.specialties;

        dto.students = store.students.stream()
                .map(StudentDTO::from)
                .toList();

        dto.studentSubjects = store.studentSubjects.stream()
                .map(StudentSubjectDTO::from)
                .toList();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(dto, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Грешка при запис на файл: " + e.getMessage());
            return false;
        }
    }
}

