public class Exit extends Operation {
    @Override
    public void execute(String[] args) {
        System.out.print("Всички незапазени промени ще бъдат премахнати. Продължи? (Y/N): ");
        String decision = scanner.nextLine().trim();

        if (decision.equalsIgnoreCase("Y")) {
            shouldExit = true;
        } else {
            System.out.println("Изходът е отменен.");
        }
    }
}
