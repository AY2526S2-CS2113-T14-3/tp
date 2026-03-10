package seedu.unitasker;

import java.util.Arrays;
import java.util.Scanner;

import seedu.unitasker.tasklist.CategoryList;

public class UniTasker {
    private static CategoryList categories;

    UniTasker() {
        categories = new CategoryList();
    }

    public static void handleMark(String[] sentence, boolean isMark) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            if (isMark) {
                categories.markTodo(categoryIndex, todoIndex);
            } else {
                categories.unmarkTodo(categoryIndex, todoIndex);
            }
            break;
        default:
            break;
        }
    }

    public static void handleDelete(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            int deleteIndex = Integer.parseInt(sentence[2]);
            categories.deleteCategory(deleteIndex - 1);
            break;
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            categories.deleteTodo(categoryIndex, todoIndex);
        default:
            break;
        }
    }

    public static void handleAdd(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            String[] nameArr = Arrays.copyOfRange(sentence, 2, sentence.length);
            String name = String.join(" ", nameArr);
            categories.addCategory(name);
            break;
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
            String description = String.join(" ", descriptionArr);
            categories.addTodo(categoryIndex, description);
        default:
            break;
        }
    }

    public static void handleReorder(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            int categoryIndex1 = Integer.parseInt(sentence[2]) - 1;
            int categoryIndex2 = Integer.parseInt(sentence[3]) - 1;
            categories.reorderCategory(categoryIndex1, categoryIndex2);
            break;
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
            int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
            categories.reorderTodo(categoryIndex, todoIndex1, todoIndex2);
            break;
        default:
            break;
        }
    }

    public static void handlePriority(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            int priority = Integer.parseInt(sentence[4]);
            categories.setTodoPriority(categoryIndex, todoIndex, priority);
            break;
        default:
            break;
        }
    }

    public static void handleList(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            System.out.println(categories);
            break;
        default:
            break;
        }
    }


    public void run() {
        System.out.println("Welcome to UniTasker");
        Scanner in = new Scanner(System.in);
        while (true) {
            String line;
            line = in.nextLine();
            String[] sentence = line.split(" ");
            String commandWord = sentence[0];
            switch (commandWord) {
            case "exit":
                System.out.println("Exiting UniTasker.");
                return;
            case "add":
                handleAdd(sentence);
                break;
            case "delete":
                handleDelete(sentence);
                break;
            case "list":
                handleList(sentence);
                break;
            case "mark":
                handleMark(sentence, true);
                break;
            case "unmark":
                handleMark(sentence, false);
                break;
            case "reorder":
                handleReorder(sentence);
                break;
            case "priority":
                handlePriority(sentence);
                break;
            default:
                System.out.println("default echo: " + line);
                break;
            }
        }
    }

    public static void main(String[] args) {
        new UniTasker().run();
    }
}
