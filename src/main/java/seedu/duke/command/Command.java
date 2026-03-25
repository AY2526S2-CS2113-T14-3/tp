package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;

public interface Command {
    void execute(AppContainer container);
}
