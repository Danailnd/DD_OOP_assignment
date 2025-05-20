import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonSerializeHelper {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> boolean saveToFile(List<T> data, String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                System.out.println("Директорията не беше намерена: " + parent.getAbsolutePath());
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(data, writer);
            }

            return true;

        } catch (IOException e) {
            System.out.println("Грешка при записване на файл: " + e.getMessage());
            return false;
        }
    }
}
