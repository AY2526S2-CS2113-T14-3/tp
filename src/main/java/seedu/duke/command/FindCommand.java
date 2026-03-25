package seedu.duke.command;

import java.util.Arrays;
import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.TaskUi;

public class FindCommand implements Command {
    private final String[] sentence;

    public FindCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length <= 1) {
            ErrorUi.printError("Find command failed: missing string input.");
            return;
        }
        String[] split = Arrays.copyOfRange(sentence, 1, sentence.length);
        String input = String.join(" ", split);
        TaskUi.printFindResults(container.getCategories().returnFoundTasks(input));
    }
}
