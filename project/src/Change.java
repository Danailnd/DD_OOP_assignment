public class Change extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: change <факултетен номер>");
            return;
        }

        String fn = args[1].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        System.out.print("Опция за промяна (specialty, group, year): ");
        String option = scanner.nextLine().trim();

        System.out.print("Нова стойност: ");
        String value = scanner.nextLine().trim();

        try {
            student.applyChange(option, value, g_data_store.specialties);
            System.out.println("Промяната е приложена успешно.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
