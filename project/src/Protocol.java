import java.util.List;

/**
 * Команда за извеждане на протоколи за дисциплина.
 *
 * Употреба: protocol <име_на_дисциплина>
 *
 * Извежда списък с всички записани студенти, подредени по специалност и курс.
 */

public class Protocol extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: protocol <име на дисциплина>");
            return;
        }

        String courseName = args[1].trim();

        List<Subject> allSubjects = g_data_store.specialties.stream()
                .flatMap(s -> s.getCourses().stream())
                .toList();

        Subject subject = Subject.findByName(allSubjects, courseName);
        if (subject == null) {
            System.out.println("Не е намерена дисциплина с име: " + courseName);
            return;
        }

        subject.printProtocol(g_data_store.students);
    }
}
