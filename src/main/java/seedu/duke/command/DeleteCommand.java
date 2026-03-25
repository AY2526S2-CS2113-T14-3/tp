package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.task.Event;
import seedu.duke.ui.CategoryUi;
import seedu.duke.ui.DeadlineUi;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.EventUi;
import seedu.duke.ui.TaskUi;

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
            case "event":
                if (sentence[3].equalsIgnoreCase("all")) {
                    container.getCategories().deleteAllEvents(categoryIndex);
                    DeadlineUi.printItemDeleted("event", null, categoryIndex);
                } else {
                    int eventIndex = Integer.parseInt(sentence[3]) - 1;
                    container.getCategories().deleteEvent(categoryIndex, eventIndex);
                    DeadlineUi.printItemDeleted("event", eventIndex, categoryIndex);
                }
                break;
            case "recurring":
                int groupIndex = Integer.parseInt(sentence[3]);
                Event eventToDelete = container.getCategories().findRecurringEventToDelete(categoryIndex, groupIndex);
                if (eventToDelete == null) {
                    throw new UniTaskerException("Choose a positive integer that represents" +
                            " the group number that belongs to the category");
                }
                container.getCategories().deleteRecurringEvent(categoryIndex, groupIndex);
                EventUi.printRecurringEventDeleted(eventToDelete);
                break;
            default:
                ErrorUi.printUnknownCommand("delete",
                        "category/todo/deadline/event [index] or delete recurring [category index] [group number]");
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
        } catch (Exception e) {
            ErrorUi.printError("An unexpected error occurred", e.getMessage());
        }
    }
}
