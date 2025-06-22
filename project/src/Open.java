import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Open extends Operation {

    private static void open(String fileName) {
        if (fileName.equals("--new")) {
            g_data_store = new DataStore();
            g_data_store.filePath = null;
            System.out.println("Създадено е ново празно хранилище на данни.");
            return;
        }

        try {
            DataStore store = JsonDeserializeHelper.loadFromFile(fileName);
            if (store != null) {
                store.filePath = fileName;
                g_data_store = store;
                System.out.println("Данните са успешно заредени от: " + fileName);
            } else {
                System.out.println("Неуспешно зареждане на данни.");
            }
        } catch (Exception e) {
            System.out.println("Грешка при отваряне: " + e.getMessage());
        }
    }
    @Override
    public void execute(String[] args) {
        assert args.length >= 2;
        open(args[1]);
    }
}
