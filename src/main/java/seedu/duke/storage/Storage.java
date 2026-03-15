package seedu.duke.storage;

import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.format.DateTimeFormatter;

/**
 * Handles the loading and saving of task data to local text files.
 * This class ensures that user data persists across different sessions by
 * serializing {@code CategoryList} objects into a readable pipe-delimited format.
 */
public class Storage {
    private String todoFilePath;
    private String deadlineFilePath;
    private String eventFilePath;

    public Storage(String todoPath, String deadlinePath,String eventPath) {
        this.todoFilePath = todoPath;
        this.deadlineFilePath = deadlinePath;
        this.eventFilePath = eventPath;
    }

    /**
     * Serializes and writes the current state of the {@code CategoryList} to the disk.
     * Overwrites existing files with the updated task information.
     *
     * @param categoryList The list of categories and tasks to save.
     * @throws IOException If there is an error writing to any of the files.
     */
    public void save(CategoryList categoryList) throws IOException {
        FileWriter todoWriter = new FileWriter(todoFilePath);
        FileWriter deadlineWriter = new FileWriter(deadlineFilePath);
        FileWriter eventWriter = new FileWriter(eventFilePath);

        for (int i = 0; i < categoryList.getAmount(); i++) {
            Category cat = categoryList.getCategory(i);

            for (int j = 0; j < cat.getTodoList().getSize(); j++) {
                todoWriter.write(cat.getName() + " | "
                        + cat.getTodoList().get(j).toFileFormat() + System.lineSeparator());
            }

            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                deadlineWriter.write(cat.getName() + " | "
                        + cat.getDeadlineList().get(j).toFileFormat() + System.lineSeparator());
            }

            // Save Events from this category
            for (int j = 0; j < cat.getEventList().getSize(); j++) {
                eventWriter.write(cat.getName() + " | "
                        + cat.getEventList().get(j).toFileFormat() + System.lineSeparator());
            }
        }
        todoWriter.close();
        deadlineWriter.close();
        eventWriter.close();
    }

    /**
     * Reads task data from the local storage files and populates the provided {@code CategoryList}.
     * If a file does not exist, it skips that specific loading process.
     *
     * @param categoryList The list to be populated with data from the files.
     */
    public void load(CategoryList categoryList) {
        File todoFile = new File(todoFilePath);
        File deadlineFile = new File(deadlineFilePath);
        File eventFile = new File(eventFilePath);

        if (todoFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(todoFile)) {
                while (s.hasNextLine()) {
                    String[] parts = s.nextLine().split(" \\| ");

                    if (parts.length < 5) {
                        continue;
                    }

                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String priority = parts[3];
                    String desc = parts[4];

                    ensureCategoryExists(categoryList, catName);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addTodo(catIdx, desc);
                    if (isDone) {
                        categoryList.markTodo(catIdx, categoryList.getCategory(catIdx).getTodoList().getSize() - 1);
                    }
                    int priorityInt = Integer.parseInt(priority);
                    categoryList.setTodoPriority(catIdx,
                            categoryList.getCategory(catIdx).getTodoList().getSize() - 1,
                            priorityInt);

                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Todo file found.");
            }
        }

        if (deadlineFile.exists()) {

            try (java.util.Scanner s = new java.util.Scanner(deadlineFile)) {
                while (s.hasNextLine()) {
                    String[] parts = s.nextLine().split(" \\| ");

                    if (parts.length < 5) {
                        continue;
                    }

                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String desc = parts[3];
                    String dateString = parts[4];

                    java.time.LocalDateTime by;

                    try {
                        by = seedu.duke.task.Deadline.parseDateTime(dateString);
                    } catch (java.time.format.DateTimeParseException e) {
                        // Log the error and skip this specific task if parsing fails completely
                        System.out.println("Skipping malformed deadline: " + desc);
                        continue;
                    }

                    // Ensure category exists
                    ensureCategoryExists(categoryList, catName);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addDeadline(catIdx, desc, by);
                    if (isDone) {
                        categoryList.setDeadlineStatus(catIdx,
                                categoryList.getCategory(catIdx).getDeadlineList().getSize() - 1,
                                true);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Deadline file found.");
            }
        }
        if (eventFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(eventFile)) {
                while (s.hasNextLine()) {
                    String[] parts = s.nextLine().split(" \\| ");

                    if (parts.length < 6) {
                        continue;
                    }

                    // Format: Category | E | Status | Description | Date (from) | Date (to)
                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String desc = parts[3];
                    String stringFrom = parts[4];
                    String stringTo = parts[5];

                    // Ensure category exists
                    ensureCategoryExists(categoryList, catName);
                    // Convert the string dateTime to dateTime objects
                    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                    java.time.LocalDateTime from = java.time.LocalDateTime.parse(stringFrom, storageFormatter);
                    java.time.LocalDateTime to = java.time.LocalDateTime.parse(stringTo, storageFormatter);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addEvent(catIdx,desc,from,to);
                    if (isDone) {
                        categoryList.setEventStatus(catIdx,
                                categoryList.getCategory(catIdx).getEventList().getSize() - 1, true);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Event file found.");
            }
        }
    }

    private void ensureCategoryExists(CategoryList categoryList, String catName) {
        if (!categoryExists(categoryList, catName)) {
            categoryList.addCategory(catName);
        }
    }

    private boolean categoryExists(CategoryList list, String name) {
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private int getCategoryIndex(CategoryList list, String name) {
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
