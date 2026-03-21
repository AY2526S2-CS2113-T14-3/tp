package seedu.duke.ui;

public class GeneralUi {
    public static final String DOTTED_LINE = "____________________________________________________________";

    public static void printDottedLine() {
        System.out.println(DOTTED_LINE);
    }

    public static void printWithBorder(String label, String content) {
        printDottedLine();
        if (label != null) {
            System.out.println(label);
        }
        System.out.println(content);
        printDottedLine();
    }

    public static void printWelcome(int startYear, int endYear, int dailyLimit, boolean isTestMode) {
        System.out.println("Welcome to UniTasker");
        if (!isTestMode) {
            System.out.println("Current Year Range: " + startYear + " to " + endYear);
            System.out.println("Current Daily Task Limit: " + dailyLimit);
            System.out.println("\nTo change these settings:");
            System.out.println("- Use 'limit task [number]' to update daily workload.");
            System.out.println("- Use 'limit year [number]' to extend the calendar range.");
        }
    }

    public static void printBordered(String message) {
        printDottedLine();
        System.out.println(message);
        printDottedLine();
    }

    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
