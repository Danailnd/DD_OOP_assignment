import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Operation> cmd = new HashMap<String, Operation>();
        cmd.put("open", (new Open()));
        cmd.put("close", (new Close()));
        cmd.put("save", (new Save()));

        Scanner s = new Scanner(System.in);
        while (true) {
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
    }
}
