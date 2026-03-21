package seedu.duke.ui;

public class CategoryUi {

    public static void printCategoryAdded(String name) {
        GeneralUi.printBordered("Added category: " + name);
    }

    public static void printCategoryDeleted(String name) {
        GeneralUi.printBordered("Deleted category: " + name);
    }

    public static void printAllMarkedDeleted() {
        GeneralUi.printBordered("All marked tasks deleted.");
    }

    public static void printList(String content) {
        GeneralUi.printWithBorder(null, content);
    }
}
