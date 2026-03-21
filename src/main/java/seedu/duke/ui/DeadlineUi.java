package seedu.duke.ui;

import seedu.duke.task.Deadline;

public class DeadlineUi {

    public static void printDeadlineAdded(String categoryName, Deadline deadline, int count) {
        GeneralUi.printDottedLine();
        System.out.println(" Got it. I've added this deadline to category: " + categoryName);
        System.out.println("   " + deadline);
        System.out.println(" Now you have " + count + " deadlines in this category.");
        GeneralUi.printDottedLine();
    }

    public static void printItemDeleted(String type, Integer index, int categoryIndex) {
        GeneralUi.printDottedLine();
        if (index == null) {
            System.out.println("All " + type + "s in this category have been deleted.");
        } else {
            System.out.println("Deleted " + type + " " + (index + 1)
                    + " from category " + (categoryIndex + 1));
        }
        GeneralUi.printDottedLine();
    }
}
