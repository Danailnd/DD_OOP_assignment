public class AddGrade extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 4) {
            System.out.println("Употреба: addgrade <fn> <course> <grade>");
            return;
        }

        String fn = args[1].trim();
        String courseName = args[2].trim();
        float grade;

        try {
            grade = Float.parseFloat(args[3].trim());
        } catch (NumberFormatException e) {
            System.out.println("Невалидна стойност за оценка.");
            return;
        }

        Student student = Student.findByFacultyNumber(g_data_store.students, fn);
        if (student == null) {
            System.out.println("Студент с този факултетен номер не е намерен.");
            return;
        }

        StudentSubject enrollment = student.findEnrollmentBySubjectName(courseName);
        if (enrollment == null) {
            System.out.println("Студентът не е записан в дисциплината \"" + courseName + "\".");
            return;
        }

        try {
            enrollment.assignGrade(grade);
            System.out.println("Оценката е добавена успешно: " + courseName + " - " + grade);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
