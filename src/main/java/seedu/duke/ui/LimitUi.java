package seedu.duke.ui;

public class LimitUi {

    public static void printDailyTaskLimitUpdated(int limit) {
        GeneralUi.printBordered("Daily task limit updated to: " + limit);
    }

    public static void printEndYearUpdated(int year) {
        GeneralUi.printBordered("Calendar end year updated to: " + year);
    }

    public static void printCurrentLimits(int startYear, int endYear, int dailyLimit) {
        GeneralUi.printDottedLine();
        System.out.println("Current Year Range: " + startYear + " to " + endYear);
        System.out.println("Current daily task limit: " + dailyLimit);
        GeneralUi.printDottedLine();
    }

    public static void printCourseResult(String result) {
        GeneralUi.printWithBorder(null, result);
    }
}
