public class Graduate extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: graduate <факултетен номер>");
            return;
        }

        String fn = args[1].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        try {
            student.graduate();
            System.out.println("Студентът е успешно отбелязан като завършил.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
