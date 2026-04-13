package seedu.duke.storage;


import static seedu.duke.UniTasker.getDailyTaskLimit;
import static seedu.duke.UniTasker.getEndYear;
import static seedu.duke.UniTasker.setDailyTaskLimit;
import static seedu.duke.UniTasker.setDailyTaskLimitSilently;
import static seedu.duke.UniTasker.setEndYear;
import static seedu.duke.UniTasker.setEndYearSilently;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.Level;

import seedu.duke.exception.UniTaskerException;
import seedu.duke.util.DateUtils;
import seedu.duke.exception.IllegalDateException;

import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import seedu.duke.ui.ErrorUi;

/**
 * Handles the loading and saving of task data to local text files.
 * This class ensures that user data persists across different sessions by
 * serializing {@code CategoryList} objects into a readable pipe-delimited format.
 */
public class Storage {

    private static final String SETTINGS_FILE = "settings.txt";
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private static final String WRITE_DELIMITER = " | ";

    private String todoFilePath;
    private String deadlineFilePath;
    private String eventFilePath;

    public Storage(String todoPath, String deadlinePath, String eventPath) {
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
        assert categoryList != null : "CategoryList should not be null when saving";
        logger.info("Starting save process...");
        ensureParentDirExists(todoFilePath);
        ensureParentDirExists(deadlineFilePath);
        ensureParentDirExists(eventFilePath);
        try (FileWriter todoWriter = new FileWriter(todoFilePath);
             FileWriter deadlineWriter = new FileWriter(deadlineFilePath);
             FileWriter eventWriter = new FileWriter(eventFilePath)) {

            for (int i = 0; i < categoryList.getAmount(); i++) {
                Category cat = categoryList.getCategory(i);

                if (cat == null) {
                    logger.severe("Null category at index " + i + ", skipping.");
                    continue;
                }

                todoWriter.write(cat.getName() + WRITE_DELIMITER
                        + "C" + System.lineSeparator());

                for (int j = 0; j < cat.getTodoList().getSize(); j++) {
                    String formatted = cat.getTodoList().get(j).toFileFormat();
                    if (formatted == null) {
                        logger.severe("Todo at index " + j + " in category '"
                                + cat.getName() + "' returned null toFileFormat(), skipping.");
                        continue;
                    }
                    todoWriter.write(cat.getName() + WRITE_DELIMITER
                            + cat.getTodoList().get(j).toFileFormat() + System.lineSeparator());
                }

                for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                    String formatted = cat.getDeadlineList().get(j).toFileFormat();
                    if (formatted == null) {
                        logger.severe("Deadline at index " + j + " in category '"
                                + cat.getName() + "' returned null toFileFormat(), skipping.");
                        continue;
                    }
                    deadlineWriter.write(cat.getName() + WRITE_DELIMITER
                            + cat.getDeadlineList().get(j).toFileFormat() + System.lineSeparator());
                }

                for (int j = 0; j < cat.getEventList().getSize(); j++) {
                    String formatted = cat.getEventList().get(j).toFileFormat();
                    if (formatted == null) {
                        logger.severe("Event at index " + j + " in category '"
                                + cat.getName() + "' returned null toFileFormat(), skipping.");
                        continue;
                    }
                    eventWriter.write(cat.getName() + WRITE_DELIMITER
                            + cat.getEventList().get(j).toFileFormat() + System.lineSeparator());
                }
            }
            logger.info("Data successfully saved to files.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write to storage files.", e);
            throw e;
        }
    }

    /**
     * Reads task data from the local storage files and populates the provided {@code CategoryList}.
     * If a file does not exist, it skips that specific loading process.
     *
     * @param categoryList The list to be populated with data from the files.
     */
    public void load(CategoryList categoryList) {
        assert categoryList != null : "CategoryList should not be null when loading";
        logger.info("Loading data from disk...");

        File todoFile = new File(todoFilePath);
        File deadlineFile = new File(deadlineFilePath);
        File eventFile = new File(eventFilePath);

        if (todoFile.exists()) {
            loadTodos(categoryList, todoFile);
        }

        if (deadlineFile.exists()) {
            loadDeadlines(categoryList, deadlineFile);
        } else {
            logger.info("No deadline file found at " + deadlineFilePath + ". Skipping load.");
        }

        if (eventFile.exists()) {
            loadEvents(categoryList, eventFile);
        }
    }

    private void loadTodos(CategoryList categoryList, File todoFile) {
        try (java.util.Scanner s = new java.util.Scanner(todoFile)) {
            int lineNumber = 0;
            while (s.hasNextLine()) {
                lineNumber++;
                String line = s.nextLine();
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }

                //split on bare "|" then trim every field; tolerates missing/extra spaces.
                String[] parts = splitAndTrim(line);

                // Strip BOM from the first field (present when file saved on Windows with Notepad).
                parts[0] = stripBom(parts[0]);

                if (parts.length == 2 && parts[1].equalsIgnoreCase("C")) {
                    categoryList.addCategory(parts[0].trim());
                    continue;
                }

                if (parts.length < 5) {
                    logger.warning("Skipping malformed line " + lineNumber + " in todos.txt: " + line);
                    continue;
                }

                String catName = parts[0];
                boolean isDone = parts[2].equals("1") || parts[2].equalsIgnoreCase("true");
                String priority = parts[3];
                String desc = parts[4];

                ensureCategoryExists(categoryList, catName);
                int catIdx = getCategoryIndex(categoryList, catName);
                if (catIdx == -1) {
                    logger.severe("Could not find or create category '" + catName + "', skipping todo.");
                    continue;
                }
                categoryList.addTodo(catIdx, desc);
                if (isDone) {
                    categoryList.markTodo(catIdx,
                            categoryList.getCategory(catIdx).getTodoList().getSize() - 1);
                }
                try {
                    categoryList.setTodoPriority(catIdx,
                            categoryList.getCategory(catIdx).getTodoList().getSize() - 1,
                            Integer.parseInt(priority));
                } catch (NumberFormatException e) {
                    logger.severe("Invalid priority value '" + priority + "' for todo '"
                            + desc + "', skipping priority assignment.");
                }
            }
        } catch (java.io.FileNotFoundException e) {
            logger.severe("Todo file not found during load.");
        } catch (UniTaskerException e) {
            ErrorUi.printError("Load error", e.getMessage());
        }
    }

    private void loadDeadlines(CategoryList categoryList, File deadlineFile) {
        logger.info("Attempting to load deadlines from: " + deadlineFilePath);
        try (java.util.Scanner s = new java.util.Scanner(deadlineFile)) {
            int lineCount = 0;
            while (s.hasNextLine()) {
                lineCount++;
                String line = s.nextLine();

                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = splitAndTrim(line);
                parts[0] = stripBom(parts[0]);

                if (parts.length < 5) {
                    logger.log(Level.SEVERE, "Skipping malformed line " + lineCount + " in deadlines.txt");
                    continue;
                }

                String catName = parts[0].trim();
                boolean isDone = parts[2].trim().equals("1") || parts[2].equalsIgnoreCase("true");
                String desc = parts[3].trim();
                String dateString = parts[4].trim();

                LocalDateTime by;

                try {
                    by = DateUtils.parseDateTimeFromFile(dateString);
                } catch (IllegalDateException e) {
                    String msg = "Deadline on line " + lineCount + " skipped — bad date format: '"
                            + dateString + "'. Expected dd-MM-yyyy HHmm.";
                    logger.severe(msg);
                    ErrorUi.printError("Warning", msg);
                    continue;
                }

                ensureCategoryExists(categoryList, catName);
                int catIdx = getCategoryIndex(categoryList, catName);
                if (catIdx == -1) {
                    logger.severe("Could not find or create category '" + catName
                            + "', skipping deadline on line " + lineCount + ".");
                    continue;
                }
                assert catIdx >= 0 : "Category index should be valid for " + catName;

                categoryList.addDeadline(catIdx, desc, by);
                if (isDone) {
                    categoryList.setDeadlineStatus(catIdx,
                            categoryList.getCategory(catIdx).getDeadlineList().getSize() - 1,
                            true);
                }
            }
            logger.info("Successfully loaded deadlines from file.");
        } catch (java.io.FileNotFoundException e) {
            logger.log(Level.SEVERE, "Deadline file cannot be found", e);
        }
    }

    private void loadEvents(CategoryList categoryList, File eventFile) {
        logger.info("Attempting to load events from: " + eventFilePath);
        DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
        int lineNumber = 0;
        try (java.util.Scanner s = new java.util.Scanner(eventFile)) {
            while (s.hasNextLine()) {
                lineNumber++;
                String line = s.nextLine();

                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = splitAndTrim(line);
                parts[0] = stripBom(parts[0]);

                if (parts.length < 6) {
                    logger.log(Level.SEVERE, "Skipping malformed line " + lineNumber + " in events.txt");
                    continue;
                }

                // Format: Category | E | Status | Description | Date (from) | Date (to) [| recurringId]
                String catName = parts[0];
                boolean isDone = parts[2].equals("1") || parts[2].equalsIgnoreCase("true");
                String desc = parts[3];
                String stringFrom = parts[4];
                String stringTo = parts[5];
                String recurringId = (parts.length > 6) ? parts[6] : "";

                LocalDateTime from;
                LocalDateTime to;
                try {
                    from = LocalDateTime.parse(stringFrom, storageFormatter);
                    to = LocalDateTime.parse(stringTo, storageFormatter);
                } catch (DateTimeParseException e) {
                    String msg = "Event on line " + lineNumber + " skipped — bad date format. "
                            + "Expected dd-MM-yyyy HHmm. Got: from='" + stringFrom
                            + "' to='" + stringTo + "'.";
                    logger.severe(msg + " " + e.getMessage());
                    ErrorUi.printError("Warning", msg);

                    continue;
                }

                ensureCategoryExists(categoryList, catName);
                int catIdx = getCategoryIndex(categoryList, catName);
                if (catIdx == -1) {
                    logger.severe("Could not find or create category '" + catName
                            + "', skipping event on line " + lineNumber + ".");
                    continue;
                }
                if (!recurringId.isEmpty() && parts[1].equalsIgnoreCase("RE")) {
                    try {
                        int recurringGroupId = Integer.parseInt(recurringId);
                        categoryList.addRecurringWeeklyEventFile(catIdx, desc, from, to, recurringGroupId);
                    } catch (NumberFormatException e) {
                        logger.severe("Could not parse recurring group ID from line: "
                                + lineNumber + " " + e.getMessage());
                        continue;
                    }
                } else {
                    categoryList.addEvent(catIdx, desc, from, to);
                }
                if (isDone) {
                    categoryList.setEventStatus(catIdx,
                            categoryList.getCategory(catIdx).getEventList().getSize() - 1, true);
                }
            }
            logger.info("Successfully loaded events from file.");
        } catch (java.io.FileNotFoundException e) {
            logger.log(Level.SEVERE, "Event file cannot be found", e);
        }
    }

    /**
     * Loads application settings from the settings file into static state.
     *
     * <p>Recognised keys: {@code endYear}, {@code dailyTaskLimit}.
     * Unknown keys are logged as warnings. If the settings file does not
     * exist the method returns silently and leaves defaults unchanged.
     *
     * <p>Each line must follow the format {@code key=value}.
     */
    public static void loadSettings() {
        java.io.File file = new java.io.File(SETTINGS_FILE);
        if (!file.exists()) {
            return; // use defaults if no settings file yet
        }
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("=", 2);

                if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    logger.severe("Skipping malformed settings line: '" + line + "'");
                    continue;
                }

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                case "endYear":
                    try {
                        setEndYearSilently(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        logger.severe("Invalid value for endYear: '" + value + "', keeping default.");
                    }
                    break;
                case "dailyTaskLimit":
                    try {
                        setDailyTaskLimitSilently(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        logger.severe("Invalid value for dailyTaskLimit: '" + value + "', keeping default.");
                    }
                    break;
                default:
                    logger.severe("Unknown setting key: " + key);
                }

            }
        } catch (IOException e) {
            ErrorUi.printError("Failed to load settings: " + e.getMessage());
        }
    }

    private static String[] splitAndTrim(String line) {
        String[] parts = line.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    private static String stripBom(String s) {
        if (s != null && s.startsWith("\uFEFF")) {
            return s.substring(1);
        }
        return s;
    }

    private static void ensureParentDirExists(String filePath) {
        File parent = new File(filePath).getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created) {
                logger.severe("Could not create parent directory for: " + filePath);
            }
        }
    }

    /**
     * Persists the current application settings to the settings file.
     *
     * <p>Writes {@code endYear} and {@code dailyTaskLimit} as
     * {@code key=value} lines, overwriting any previous content.
     * Errors are reported via {@link ErrorUi} and do not propagate.
     */
    public static void saveSettings() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(SETTINGS_FILE))) {
            writer.println("endYear=" + getEndYear());
            writer.println("dailyTaskLimit=" + getDailyTaskLimit());
        } catch (IOException e) {
            ErrorUi.printError("Failed to save settings: " + e.getMessage());
        }
    }

    private void ensureCategoryExists(CategoryList categoryList, String catName) {
        if (!categoryExists(categoryList, catName)) {
            categoryList.addCategory(catName);
        }
    }

    private boolean categoryExists(CategoryList list, String name) {
        String trimmed = name.trim();
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().trim().equals(trimmed)) {
                return true;
            }
        }
        return false;
    }

    private int getCategoryIndex(CategoryList list, String name) {
        String trimmed = name.trim();
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().trim().equals(trimmed)) {
                return i;
            }
        }
        return -1;
    }
}
