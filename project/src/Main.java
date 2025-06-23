import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Operation> cmd = new HashMap<String, Operation>();
        cmd.put("open", (new Open()));
        cmd.put("close", (new Close()));
        cmd.put("save", (new Save()));
        cmd.put("saveas", (new SaveAs()));
        cmd.put("exit", (new Exit()));
        cmd.put("enroll", new Enroll());
        cmd.put("change", new Change());
        cmd.put("graduate", new Graduate());
        cmd.put("interrupt", new Interrupt());
        cmd.put("print", new Print());
        cmd.put("printall", new PrintAll());
        cmd.put("resume", new Resume());
        cmd.put("advance", new Advance());
        cmd.put("enrollin", new EnrollIn());
        cmd.put("grade", new AddGrade());
        cmd.put("protocol", new Protocol());
        cmd.put("report", new Report());
        cmd.put("help", new Help());

        Scanner s = new Scanner(System.in);
        Operation.setScanner(s);
        while (!Operation.shouldExit) {
            System.out.print("> ");
            String input = s.nextLine();
            String[] arg = input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            Operation action = cmd.get(arg[0]);
            if (action == null) {
                System.out.println("Invalid command");
                continue;
            }
            try {
                action.execute(arg);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        s.close();
        System.out.println("Програмата приключи.");
    }
}
