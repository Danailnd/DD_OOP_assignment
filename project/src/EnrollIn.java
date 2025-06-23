public class EnrollIn extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 3) {
            System.out.println("Употреба: enrollin <fn> <course>");
            return;
        }

        String fn = args[1].trim();
        String subjectName = args[2].trim();

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        Subject subject = student.getSpecialty().findSubjectByName(subjectName);
        if (subject == null) {
            System.out.println("Дисциплината не е част от специалността на студента.");
            return;
        }

        try {
            StudentSubject ss = new StudentSubject(student, subject, -1);
            student.enrollInSubject(ss);
            System.out.println("Успешно записване в дисциплина: " + subjectName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
