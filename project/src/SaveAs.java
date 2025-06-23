/**
 * Команда за запазване на текущото хранилище в нов файл.
 *
 * Употреба: saveas <път_до_файл>
 *
 * След успешно запазване актуализира filePath в DataStore.
 */

public class SaveAs extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни за запис.");
            return;
        }

        if (args.length < 2 || args[1].isBlank()) {
            System.out.println("Употреба: saveas <име на файл>");
            return;
        }

        String newPath = args[1];
        boolean success = JsonSerializeHelper.saveToFile(g_data_store, newPath);

        if (success) {
            g_data_store.filePath = newPath;
            System.out.println("Данните са успешно записани в: " + newPath);
        } else {
            System.out.println("Грешка при запис на файла.");
        }
    }
}
