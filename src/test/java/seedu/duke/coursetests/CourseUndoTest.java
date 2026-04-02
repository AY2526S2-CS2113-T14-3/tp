package seedu.duke.coursetests;

import org.junit.jupiter.api.Test;
import seedu.duke.course.CourseManager;
import seedu.duke.command.CourseCommand;
import seedu.duke.appcontainer.AppContainer;
import seedu.duke.command.CourseParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import seedu.duke.command.Command;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseUndoTest {

    private String createTempFilePath() throws Exception {
        Path tempFile = Files.createTempFile("course-undo-test-", ".txt");
        tempFile.toFile().deleteOnExit();
        return tempFile.toString();
    }

    private AppContainer createContainer() throws Exception {
        CourseManager courseManager = new CourseManager(createTempFilePath());
        CourseParser courseParser = new CourseParser(courseManager);
        return new AppContainer(null, null, null, courseParser);
    }

    @Test
    public void undoAddCourse_courseRemoved_success() throws Exception {
        AppContainer container = createContainer();
        Stack<Command> history = new Stack<>();

        CourseCommand addCommand = new CourseCommand("course add CS2113");
        addCommand.execute(container);
        if (addCommand.isUndoable()) {
            history.push(addCommand);
        }

        assertEquals(1, container.courseParser().getCourseManager().getCourseList().size());

        Command last = history.pop();
        last.undo(container);

        assertEquals(0, container.courseParser().getCourseManager().getCourseList().size());
    }

    @Test
    public void undoDeleteCourse_courseRestored_success() throws Exception {
        AppContainer container = createContainer();
        Stack<Command> history = new Stack<>();

        CourseCommand addCommand = new CourseCommand("course add CS2113");
        addCommand.execute(container);

        CourseCommand deleteCommand = new CourseCommand("course delete CS2113");
        deleteCommand.execute(container);
        if (deleteCommand.isUndoable()) {
            history.push(deleteCommand);
        }

        assertEquals(0, container.courseParser().getCourseManager().getCourseList().size());

        Command last = history.pop();
        last.undo(container);

        assertEquals(1, container.courseParser().getCourseManager().getCourseList().size());
    }

    @Test
    public void undoAddAssessment_assessmentRemoved_success() throws Exception {
        AppContainer container = createContainer();
        Stack<Command> history = new Stack<>();

        new CourseCommand("course add CS2113").execute(container);

        CourseCommand addAssessment = new CourseCommand(
                "course add-assessment CS2113 /n Finals /w 40 /ms 100");
        addAssessment.execute(container);
        if (addAssessment.isUndoable()) {
            history.push(addAssessment);
        }

        assertEquals(1, container.courseParser().getCourseManager()
                .getCourseList().get("CS2113").getAssessmentCount());

        Command last = history.pop();
        last.undo(container);

        assertEquals(0, container.courseParser().getCourseManager()
                .getCourseList().get("CS2113").getAssessmentCount());
    }

    @Test
    public void multipleUndos_correctOrder_success() throws Exception {
        AppContainer container = createContainer();
        Stack<Command> history = new Stack<>();

        CourseCommand addCS2113 = new CourseCommand("course add CS2113");
        addCS2113.execute(container);
        if (addCS2113.isUndoable()) {
            history.push(addCS2113);
        }

        CourseCommand addEE2211 = new CourseCommand("course add EE2211");
        addEE2211.execute(container);
        if (addEE2211.isUndoable()) {
            history.push(addEE2211);
        }

        assertEquals(2, container.courseParser().getCourseManager().getCourseList().size());

        history.pop().undo(container);
        assertEquals(1, container.courseParser().getCourseManager().getCourseList().size());

        history.pop().undo(container);
        assertEquals(0, container.courseParser().getCourseManager().getCourseList().size());
    }
}
