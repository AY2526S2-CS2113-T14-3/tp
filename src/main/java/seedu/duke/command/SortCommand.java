package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.exception.UniTaskerException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class SortCommand implements Command {
    private final String[] sentence;

    public SortCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= 1) {
            ErrorUi.printUnknownCommand("sort", "todo");
            return;
        }

        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            try {
                if (sentence.length <= 2) {
                    throw new UniTaskerException("Insufficient arguments");
                }
                int categoryIndex = CommandSupport.getCategoryIndex(container, sentence);
                container.getCategories().getCategory(categoryIndex).getTodoList().sortByPriority();
                TaskUi.printSortedByPriority(categoryIndex);
            } catch (Exception e) {
                ErrorUi.printCommandFailed("sort todo", e.getMessage(), "sort todo [catIndex]");
            }
            break;
        default:
            ErrorUi.printUnknownCommand("sort", "todo");
            break;
        }

        CommandSupport.saveData(container);
    }
}
