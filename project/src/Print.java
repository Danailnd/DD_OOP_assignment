public class Print extends Operation {
    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Употреба: print <факултетен номер>");
            return;
        }

        String fn = args[1];
        Student student = Student.findByFacultyNumber(g_data_store.students, fn);

        if (student == null) {
            System.out.println("Студент с факултетен номер " + fn + " не е намерен.");
            return;
        }

        student.printAcademicReport();
    }
}
