/**
 * Команда за промяна на специалност, курс или група на студент.
 *
 * Употреба: change <факултетен_номер> <опция> <стойност>
 *
 * Опциите могат да бъдат:
 * - specialty: сменя специалността, ако условията позволяват
 * - group: прехвърля студента в друга група
 * - year: повишава студента в курс, ако има право
 */

public class Change extends Operation {
    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 4) {
            System.out.println("Употреба: change <факултетен_номер> <опция> <стойност>");
            return;
        }

        String fn = args[1].trim();
        String option = args[2].trim();
        String value = args[3].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.applyChange(option, value, g_data_store.specialties);
            System.out.println("Промяната е приложена успешно.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
