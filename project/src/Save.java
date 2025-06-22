public class Save extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни за запис.");
            return;
        }

        if (g_data_store.filePath == null) {
            System.out.println("Файлът не е указан. Използвайте 'saveas <име на файл>' за първоначално записване.");
            return;
        }
        boolean success = JsonSerializeHelper.saveToFile(g_data_store, g_data_store.filePath);
        if (success) {
            System.out.println("Данните са успешно записани в: " + g_data_store.filePath);
        } else {
            System.out.println("Грешка при запис на файла.");
        }
    }
}
