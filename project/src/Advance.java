/**
 * Команда за повишаване на студент в следващ курс.
 *
 * Употреба: advance <факултетен_номер>
 *
 * Проверява дали студентът има право да премине напред.
 */

public class Advance extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: advance <факултетен номер>");
            return;
        }

        String fn = args[1].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.advanceToNextYear();
            System.out.println("Студентът е преместен в следващ курс.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
