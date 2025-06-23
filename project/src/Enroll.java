/**
 * Команда за записване на нов студент.
 *
 * Употреба: enroll <факултетен_номер> <специалност> <група> <име>
 *
 * Създава студент в първи курс със зададените параметри и го добавя в хранилището.
 */

public class Enroll extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }
        if (args.length < 5) {
            System.out.println("Липсват аргументи. Очаква се: enroll <fn> <spec> <group> <name>");
            return;
        }

        String fn = args[1].trim();
        String programName = args[2].trim();
        int group;
        try {
        group = Integer.parseInt(args[3].trim());
        } catch (NumberFormatException e) {
            System.out.println("Невалиден номер на група.");
            return;
        }
        String name = args[4].trim();

        try {
            Student newStudent = Student.enroll(name, fn, programName, group, g_data_store.specialties);
            g_data_store.students.add(newStudent);
            System.out.printf(
                    "Успешно записан студент %s в 1 курс на специалност %s, група %d, ФН: %s%n",
                    name, programName, group, fn
            );
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
