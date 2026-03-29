package seedu.duke.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.GeneralUi;
import seedu.duke.ui.LimitUi;
import seedu.duke.util.DateUtils;
import seedu.duke.exception.IllegalDateException;

public class ListCommand implements Command {
    private final String[] sentence;

    public ListCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < 2) {
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, range, " +
                    "recurring, occurrence or limit");
            return;
        }

        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            handleListCategory(container);
            break;
        case "todo":
            CategoryUi.printList(container.getCategories().getAllTodos());
            break;
        //@@author WenJunYu5984
        case "deadline":
            CategoryUi.printList(container.getCategories().getAllDeadlines());
            break;
        //@@author sushmiithaa
        case "event":
            boolean showAll = (sentence.length > 2 && sentence[2].equalsIgnoreCase("/all"));
            GeneralUi.printWithBorder(null, container.getCategories().getAllEvents(showAll));
            break;
        //@@author WenJunYu5984
        case "range":
            handleListRange(container);
            break;
        //@@author sushmiithaa
        case "recurring":
            GeneralUi.printWithBorder(null, container.getCategories().getAllRecurringEvents());
            break;
        case "occurrence":
            try {
                int catIdx = CommandSupport.getCategoryIndex(container, sentence);
                String currentView = container.getCategories().getCurrentView();
                if (!currentView.equals("EVENT")){
                    throw new UniTaskerException("Please use: list event first before list occurrence");
                }
                int recurringUiIdx = Integer.parseInt(sentence[3]);
                String allRecurringEventsWithinGroup = container.getCategories()
                        .getOccurrencesOfRecurringEvent(catIdx, recurringUiIdx);
                GeneralUi.printWithBorder(null, allRecurringEventsWithinGroup);
            } catch (UniTaskerException e){
                ErrorUi.printError(e.getMessage());
            } catch (Exception e) {
                ErrorUi.printError("Please use: list event first then list occurrence [cat_idx] [event_idx]");
            }
            break;
        //@@author WenJunYu5984
        case "limit":
            LimitUi.printCurrentLimits(container.getStartYear(), container.getEndYear(), container.getDailyTaskLimit());
            break;
        //@@author
        default:
            ErrorUi.printUnknownCommand("list", "category, todo, deadline, event, " +
                    "range, recurring, occurrence or limit");
            break;
        }
    }

    private void handleListCategory(AppContainer container) {
        int sentenceLength = sentence.length;
        switch (sentenceLength) {
        case 2:
            CategoryUi.printList(container.getCategories().toString());
            break;
        case 3:
            try {
                int catIndex = CommandSupport.getCategoryIndex(container, sentence);
                CategoryUi.printList(container.getCategories().getCategory(catIndex).toString());
            } catch (Exception e) {
                ErrorUi.printCommandFailed("list category [index]", e.getMessage(), null);
            }
            break;
        default:
            ErrorUi.printMissingArgs("List command has too many arguments");
            break;
        }
    }

    //@@author WenJunYu5984
    private void handleListRange(AppContainer container) {
        try {
            LocalDate start = DateUtils.parseLocalDate(sentence[2]);
            LocalDate end = DateUtils.parseLocalDate(sentence[3]);

            if (start.getYear() < container.getStartYear() || end.getYear() > container.getEndYear()) {
                ErrorUi.printRangeOutOfBounds(container.getStartYear(), container.getEndYear());
                return;
            }
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date must be earlier than End date");
            }

            if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/deadline")) {
                container.getCalendar().displaySpecificTypeInRange(start, end, Deadline.class);
            } else if (sentence.length > 4 && sentence[4].equalsIgnoreCase("/event")) {
                container.getCalendar().displaySpecificTypeInRange(start, end, Event.class);
            } else {
                container.getCalendar().displayRange(start, end);
            }
        } catch (DateTimeParseException e) {
            ErrorUi.printRangeDateFormatError();
        } catch (ArrayIndexOutOfBoundsException e) {
            ErrorUi.printRangeMissingDates();
        } catch (IllegalArgumentException e) {
            ErrorUi.printRangeStartAfterEnd();
        } catch (IllegalDateException e) {
            ErrorUi.printError(e.getMessage());
        }
    }
    //@@author
}
