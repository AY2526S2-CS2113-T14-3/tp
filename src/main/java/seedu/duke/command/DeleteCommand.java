package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.TaskUi;
import seedu.duke.ui.GeneralUi;

import seedu.duke.tasklist.EventReference;
import java.util.List;
import static seedu.duke.tasklist.CategoryList.refreshCalendar;

public class DeleteCommand implements Command {
    private final String[] sentence;

    public DeleteCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < 2) {
            ErrorUi.printMissingArgs("Use: delete [type] [index]");
            return;
        }

        try {
            String secondCommand = sentence[1];
            int categoryIndex = -1;
            if (!secondCommand.equals("marked") && !secondCommand.equals("category")) {
                categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
            }

            switch (secondCommand) {
            case "marked":
                container.getCategories().deleteMarkedTasks();
                CategoryUi.printAllMarkedDeleted();
                break;
            case "category":
                int deleteIndex = Integer.parseInt(sentence[2]) - 1;
                String catName = container.getCategories().getCategory(deleteIndex).getName();
                container.getCategories().deleteCategory(deleteIndex);
                CategoryUi.printCategoryDeleted(catName);
                break;
            case "todo":
                int todoIndex = Integer.parseInt(sentence[3]) - 1;
                String todoName = container.getCategories().getCategory(categoryIndex)
                        .getTodo(todoIndex).getDescription();
                container.getCategories().deleteTodo(categoryIndex, todoIndex);
                TaskUi.printTaskAction("Deleted", "todo", todoName);
                break;
            //@@author WenJunYu5984
            case "deadline":
                if (sentence[3].equalsIgnoreCase("all")) {
                    container.getCategories().deleteAllDeadlines(categoryIndex);
                    DeadlineUi.printItemDeleted("deadline", null, categoryIndex);
                } else {
                    int deadlineIndex = Integer.parseInt(sentence[3]) - 1;
                    container.getCategories().deleteDeadline(categoryIndex, deadlineIndex);
                    DeadlineUi.printItemDeleted("deadline", deadlineIndex, categoryIndex);
                }
                break;
            //@@author sushmiithaa
            case "event":
                if (sentence[3].equalsIgnoreCase("all")) {
                    container.getCategories().deleteAllEvents(categoryIndex);
                    DeadlineUi.printItemDeleted("event", null, categoryIndex);
                } else {
                    int uiIndex = Integer.parseInt(sentence[3]) - 1;
                    String currentView = container.getCategories().getCurrentView();
                    List<EventReference> map = container.getCategories().getActiveDisplayMap();
                    for (int i = 0; i < map.size(); i++) {
                        System.out.println("catIndex: " + map.get(i).categoryIndex
                                + "eventIndex: " + map.get(i).eventIndex);
                    }
                    if (!(currentView.equals("EVENT") || currentView.equals("EVENT_EXPANDED"))) {
                        throw new UniTaskerException("To delete a specific event please use " +
                                "'list event' or 'list event /all' first");
                    }
                    if (uiIndex < 0 || uiIndex >= map.size()) {
                        throw new IndexOutOfBoundsException();
                    }

                    EventReference ref = map.get(uiIndex);
                    Event eventToDelete = container.getCategories().getEvent(ref.categoryIndex, ref.eventIndex);
                    if (eventToDelete.getIsRecurring() &&
                            (!container.getCategories().getCurrentView().equals("EVENT_EXPANDED"))) {
                        GeneralUi.printBordered("This is a recurring group. To delete the specific occurrence, please" +
                                "use 'list event /all' or 'list occurrence " +
                                (categoryIndex + 1) + " " + (uiIndex + 1) + "' first");
                    } else {
                        container.getCategories().deleteEvent(ref.categoryIndex, ref.eventIndex);
                        EventUi.printNormalEventDeleted(eventToDelete);
                    }
                }
                break;
            case "recurring":
                String currentView = container.getCategories().getCurrentView();
                if (!(currentView.equals("RECURRING_OVERVIEW"))) {
                    throw new UniTaskerException("To delete a the recurring event group please " +
                            "use 'list recurring' first");
                }
                int uiIndex = Integer.parseInt(sentence[3]);
                List<EventReference> displayMap = container.getCategories().getActiveDisplayMap();
                for (int i = 0; i < displayMap.size(); i++) {
                    System.out.println("catIndex: " + displayMap.get(i).categoryIndex
                            + "eventIndex: " + displayMap.get(i).eventIndex);
                }
                EventReference eventReference = displayMap.get(uiIndex - 1);
                Event event = container.getCategories().getEvent(eventReference.categoryIndex,
                        eventReference.eventIndex);

                container.getCategories().deleteRecurringEvent(categoryIndex, event.getRecurringGroupId());
                EventUi.printRecurringEventDeletedGroup(event);
                break;
            case "occurrence":
                int uiIdx = Integer.parseInt(sentence[3]) - 1;
                List<EventReference> map = container.getCategories().getActiveDisplayMap();
                for (int i = 0; i < map.size(); i++) {
                    System.out.println("catIndex: " + map.get(i).categoryIndex
                            + "eventIndex: " + map.get(i).eventIndex);
                }
                if (!container.getCategories().getCurrentView().equals("OCCURRENCE_VIEW")) {
                    GeneralUi.printBordered("Please run 'list occurrence' first to see individual dates.");
                    break;
                }
                EventReference target = map.get(uiIdx);
                Event eventToDel = container.getCategories().getEvent(target.categoryIndex, target.eventIndex);
                container.getCategories().deleteEvent(target.categoryIndex, target.eventIndex);
                EventUi.printRecurringEventDeleted(eventToDel);
                break;

            //@@author
            default:
                ErrorUi.printUnknownCommand("delete",
                        "category/todo/deadline/event [index] or " +
                                "delete recurring [category index] [index number]");
                break;
            }

            CommandSupport.saveData(container);
            refreshCalendar(container.getCategories(), container.getCalendar());
        } catch (ArrayIndexOutOfBoundsException e) {
            ErrorUi.printMissingArgs("Example: delete todo 1 1");
        } catch (NumberFormatException e) {
            ErrorUi.printInvalidNumber();
        } catch (IndexOutOfBoundsException e) {
            ErrorUi.printIndexNotFound();
        } catch (UniTaskerException e) {
            ErrorUi.printError("Error occurred: ",e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("An unexpected error occurred", e.getMessage());
        }
    }
}
