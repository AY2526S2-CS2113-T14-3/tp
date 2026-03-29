package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class MarkCommand implements Command {
    private final String[] sentence;
    private final boolean isMark;

    public MarkCommand(String[] sentence, boolean isMark) {
        this.sentence = sentence;
        this.isMark = isMark;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length < 4) {
            ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
            return;
        }

        try {
            String secondCommand = sentence[1];
            switch (secondCommand) {
            case "todo":
                handleTodo(container);
                break;
            case "deadline":
                handleDeadline(container);
                break;
            case "event":
                handleEvent(container);
                break;
            default:
                ErrorUi.printUnknownCommand("mark/unmark", "todo, deadline or event");
                break;
            }
            CommandSupport.saveData(container);
        } catch (Exception e) {
            ErrorUi.printMarkTaskError();
        }
    }
    //@@author marken9
    private void handleTodo(AppContainer container) {
        try {
            Result result = getResult(container);
            if (isMark) {
                container.getCategories().markTodo(result.categoryIndex(), result.taskIndex());
            } else {
                container.getCategories().unmarkTodo(result.categoryIndex(), result.taskIndex());
            }
            TaskUi.printMarkTodoResult(isMark, null);
        } catch (Exception e) {
            TaskUi.printMarkTodoResult(isMark, e.getMessage());
        }
    }

    private Result getResult(AppContainer container) {
        int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
        int taskIndex = Integer.parseInt(sentence[3]) - 1;
        Result result = new Result(categoryIndex, taskIndex);
        return result;
    }

    private record Result(int categoryIndex, int taskIndex) {
    }

    //@@author WenJunYu5984
    private void handleDeadline(AppContainer container) {
        try {
            Result result = getResult(container);
            container.getCategories().setDeadlineStatus(result.categoryIndex, result.taskIndex, isMark);
            TaskUi.printStatusChanged(container.getCategories()
                    .getDeadline(result.categoryIndex, result.taskIndex), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }

    //@@author sushmiithaa
    private void handleEvent(AppContainer container) {
        try {
            Result result = getResult(container);
            container.getCategories().setEventStatus(result.categoryIndex, result.taskIndex, isMark);
            TaskUi.printStatusChanged(container.getCategories()
                    .getEvent(result.categoryIndex, result.taskIndex), isMark);
        } catch (Exception e) {
            ErrorUi.printError(e.getMessage());
        }
    }
}
