package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;
import java.util.logging.Logger;

import seedu.duke.calender.Calendar;
import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

import static seedu.duke.tasklist.CategoryList.refreshCalendar;

import seedu.duke.coursestracker.CourseManager;
import seedu.duke.coursestracker.CourseParser;

import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;


import seedu.duke.appcontainer.AppContainer;
import seedu.duke.command.Command;
import seedu.duke.command.CommandParser;
import seedu.duke.command.ExitCommand;


public class UniTasker {

    private static final Logger logger = Logger.getLogger(UniTasker.class.getName());

    private static CategoryList categories = new CategoryList();
    private static Calendar calendar = new Calendar();
    private static Storage storage = new Storage("todos.txt", "deadlines.txt", "events.txt");
    private static CourseParser courseParser;

    private static int dailyTaskLimit;
    private static int startYear;
    private static int endYear;

    private static final int DEFAULT_END_YEAR = 2030;
    private static final int DEFAULT_DAILY_TASK_LIMIT = 8;
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public UniTasker() {
        try {
            storage.load(categories);
            refreshCalendar(categories, calendar);
            CourseManager courseManager = new CourseManager("courses.txt");
            courseParser = new CourseParser(courseManager);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }


    public void run(boolean isTestMode) {
        logger.info("UniTasker session started.");
        GeneralUi.printWelcome(startYear, endYear, dailyTaskLimit, isTestMode);

        AppContainer container = new AppContainer(
                categories, calendar, storage, courseParser,
                dailyTaskLimit, startYear, endYear
        );

        CommandParser parser = new CommandParser();
        Scanner in = new Scanner(System.in);

        while (true) {
            if (!in.hasNextLine()) {
                break;
            }

            String line = in.nextLine();
            Command command = parser.parse(line);

            if (command instanceof ExitCommand) {
                GeneralUi.printMessage("Exiting UniTasker.");
                return;
            }

            command.execute(container);
        }
        in.close();
    }

    public static void main(String[] args) {
        seedu.duke.logging.LogConfig.setup();
        logger.info("UniTasker is launching...");

        startYear = LocalDate.now().getYear();
        endYear = DEFAULT_END_YEAR;
        dailyTaskLimit = DEFAULT_DAILY_TASK_LIMIT;

        boolean isTestMode = args.length > 0 && args[0].equalsIgnoreCase("-test");
        new UniTasker().run(isTestMode);
    }

    public static int getEndYear() {
        return endYear;
    }

    public static void setStartYear(int year) {
        startYear = year;
    }

    public static void setEndYear(int year) {
        endYear = year;
    }

    public static void setDailyTaskLimit(int newLimit) {
        dailyTaskLimit = newLimit;
        LimitUi.printDailyTaskLimitUpdated(dailyTaskLimit);
    }


}
