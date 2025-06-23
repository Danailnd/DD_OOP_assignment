/**
 * Команда за отбелязване на студент като прекъснал.
 *
 * Употреба: interrupt <факултетен_номер>
 *
 * Прекъсналите не могат да полагат изпити или да се записват в нови дисциплини.
 */

public class Interrupt extends Operation {
    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: interrupt <факултетен номер>");
            return;
        }

        String fn = args[1].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.interrupt();
            System.out.println("Студентът е успешно отбелязан като прекъснал.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
