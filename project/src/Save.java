public class Save extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма отворени данни за запис.");
            return;
        }

        String pathToSave;

        if (args.length == 1) {
            if (g_data_store.filePath == null) {
                System.out.println("Файлът за запис не е указан. Използвайте: save <файл>");
                return;
            }
            pathToSave = g_data_store.filePath;
        } else {
            pathToSave = args[1];
            g_data_store.filePath = pathToSave;
        }

        boolean success = JsonSerializeHelper.saveToFile(g_data_store, pathToSave);
        if (success) {
            System.out.println("Данните са записани успешно в: " + pathToSave);
        } else {
            System.out.println("Грешка при запис на файла.");
        }
    }
}