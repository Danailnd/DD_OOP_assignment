import java.util.List;

public class PrintAll extends Operation {

    @Override
    public void execute(String[] args) {
        if (g_data_store == null) {
            System.out.println("Няма заредени данни.");
            return;
        }

        if (args.length < 3) {
            System.out.println("Употреба: printall <program> <year>");
            return;
        }

        String programName = args[1].trim();
        int year;
        try {
            year = Integer.parseInt(args[2].trim());
        } catch (NumberFormatException e) {
            System.out.println("Невалидна година (курс).");
            return;
        }

        // Find specialty by name
        Specialty specialty = g_data_store.specialties.stream()
                .filter(s -> s.getName().equalsIgnoreCase(programName))
                .findFirst()
                .orElse(null);

        if (specialty == null) {
            System.out.println("Специалността не е намерена: " + programName);
            return;
        }

        // Filter students in that specialty and year
        List<Student> filtered = g_data_store.students.stream()
                .filter(s -> s.getSpecialty().getId().equals(specialty.getId()) && s.getCourse() == year)
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("Няма студенти в специалност " + programName + ", курс " + year);
            return;
        }

        System.out.println("Справка за студенти в специалност " + programName + ", курс " + year + ":");
        for (Student s : filtered) {
            System.out.printf("- Име: %s | Фак. номер: %s | Група: %d | Статус: %s | Среден успех: %.2f%n",
                    s.getName(),
                    s.getFacultyNumber(),
                    s.getGroup(),
                    s.getStatus(),
                    s.getAverageGrade());
        }
    }
}
