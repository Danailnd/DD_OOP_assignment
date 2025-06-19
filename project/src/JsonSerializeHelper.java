import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Помощен клас за сериализация на данни в JSON формат и записването им във файлове.
 * <p>
 * Използва библиотеката Gson за конвертиране на Java обекти в JSON с форматиран (pretty print) изход.
 * </p>
 *
 * <p>Методът предоставя възможност за запис на списък с данни във файл.</p>
 *
 * @author Данаил Димитров
 */
public class JsonSerializeHelper {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /**
     * Записва списък от данни във JSON файл на посочения път.
     *
     * @param <T> Типът на обектите в списъка.
     * @param data Списък с данни за сериализация и запис.
     * @param filePath Път до файла, в който ще се запишат данните.
     * @return {@code true} ако записът е успешен, {@code false} при възникване на грешка.
     *
     * @throws IOException ако възникне грешка при записване във файла.
     */

    public static <T> boolean saveToFile(List<T> data, String filePath) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            System.out.println("Директорията не беше намерена: " + parent.getAbsolutePath());
            return false;
        }

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        }

        return true;
    }
}
