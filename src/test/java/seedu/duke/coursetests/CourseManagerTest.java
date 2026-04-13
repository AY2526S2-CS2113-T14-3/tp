package seedu.duke.coursetests;

import org.junit.jupiter.api.Test;
import seedu.duke.course.Assessment;
import seedu.duke.course.Course;
import seedu.duke.course.CourseManager;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import seedu.duke.exception.CourseException;

public class CourseManagerTest {

    private String createTempFilePath() throws Exception {
        Path tempFile = Files.createTempFile("course-test-", ".txt");
        tempFile.toFile().deleteOnExit();
        return tempFile.toString();
    }

    @Test
    public void addCourse_validCourse_success() throws Exception {
        CourseManager manager = new CourseManager(createTempFilePath());

        String result = manager.addCourse("CS2113");

        assertTrue(result.contains("CS2113"));
        assertEquals(1, manager.getCourseList().size());
    }

    @Test
    public void addAssessment_validAssessment_success() throws Exception {
        CourseManager manager = new CourseManager(createTempFilePath());

        manager.addCourse("CS2113");
        manager.addAssessment("CS2113", "Finals", 40, 100);

        Course course = manager.getCourseList().get("CS2113");

        assertNotNull(course.getAssessment("Finals"));
        assertEquals(1, course.getAssessmentCount());
    }

    @Test
    public void recordScore_validScore_success() throws Exception {
        CourseManager manager = new CourseManager(createTempFilePath());

        manager.addCourse("CS2113");
        manager.addAssessment("CS2113", "Finals", 40, 100);
        manager.recordScore("CS2113", "Finals", 80);

        Course course = manager.getCourseList().get("CS2113");
        Assessment assessment = course.getAssessment("Finals");

        assertEquals(80, assessment.getScoreObtained());
    }

    @Test
    public void addAssessment_maxScoreAboveLimit_throwsCourseException() throws Exception {
        CourseManager manager = new CourseManager(createTempFilePath());
        manager.addCourse("CS2113");

        CourseException exception = assertThrows(CourseException.class,
                () -> manager.addAssessment("CS2113", "Finals", 40, 10001));

        assertEquals("Maximum score cannot exceed 10000.0.", exception.getMessage());
    }
}
