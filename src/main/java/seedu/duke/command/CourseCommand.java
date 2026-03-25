package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.coursestracker.CourseException;
import seedu.duke.ui.ErrorUi;
import seedu.duke.ui.LimitUi;

public class CourseCommand implements Command {
    private final String line;

    public CourseCommand(String line) {
        this.line = line;
    }

    @Override
    public void execute(AppContainer container) {
        try {
            String courseCommand = line.substring("course".length()).trim();
            String result = container.getCourseParser().parse(courseCommand);
            LimitUi.printCourseResult(result);
        } catch (CourseException e) {
            ErrorUi.printError(e.getMessage());
        } catch (Exception e) {
            ErrorUi.printError("Could not process course command.");
        }
    }
}
