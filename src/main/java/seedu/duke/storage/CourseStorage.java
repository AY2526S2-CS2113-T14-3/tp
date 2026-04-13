package seedu.duke.storage;

import seedu.duke.course.Assessment;
import seedu.duke.course.Course;
import seedu.duke.course.CourseList;
import seedu.duke.exception.CourseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles saving and loading course data from disk.
 * Similar role to Storage in your Duke iP.
 * MICHAEL:
 * - Your manager/main app should call load() at startup
 * and save(...) after any modification.
 */
public class CourseStorage {

    private final String filePath;

    public CourseStorage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all courses from persistent storage on disk.
     *
     * File format - line-based encoding with course blocks:
     *   COURSE:CS2113
     *   Finals|40.0|85.0|100.0
     *   Midterm|20.0|-1.0|25.0
     *   Assignment|25.0|18.0|20.0
     *   END
     *   COURSE:ST2334
     *   Exam|60.0|68.0|100.0
     *   TEST|20.0|-1.0|100.0
     *   END
     *
     * Format field meanings (pipe-delimited):
     *   name - assessment name
     *   weightage - percentage weight in final grade
     *   scoreObtained - score achieved, or -1.0 (NOT_GRADED sentinel) if ungraded
     *   maxScore - maximum possible score for this assessment
     *
     * Robustness features:
     * - Returns empty list if file does not exist (first-run scenario) instead of failing
     * - Gracefully skips blank lines to handle formatting variations
     * - Skips malformed assessment lines without crashing (continues loading other data)
     * - Uses END markers to cleanly delimit course blocks, enabling multi-course files
     * - Creates parent directory automatically if missing
     *
     * Error handling rationale:
     * - IOException wrapped in CourseException for consistent error handling
     * - Partial data corruption skipped rather than causing total failure
     * - This allows recovery from minor file corruption or manual edits
     *
     * @return ArrayList of all loaded Course objects, or empty list if file missing
     * @throws CourseException if file cannot be read or parent directory creation fails
     */
    public ArrayList<Course> load() throws CourseException {
        ensureParentDirectoryExists();

        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            Course currentCourse = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("COURSE:")) {
                    String courseCode = line.substring("COURSE:".length()).trim();
                    currentCourse = new Course(courseCode);

                } else if (line.equals("END")) {
                    if (currentCourse != null) {
                        loadedCourses.add(currentCourse);
                        currentCourse = null;
                    }

                } else {
                    if (currentCourse != null) {
                        Assessment assessment = Assessment.decode(line);
                        if (assessment != null) {
                            currentCourse.addAssessment(assessment);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new CourseException("Could not load courses from disk.");
        }

        return loadedCourses;
    }

    /**
     * Saves all courses to persistent storage on disk, overwriting previous contents.
     * Iterates through CourseList and calls Course.encode() to convert each course
     * to the standard text format.
     *
     * Encoding strategy - delegates to Course.encode():
     * Each course produces a block like:
     *   COURSE:CS2113
     *   Finals|40.0|85.0|100.0
     *   Midterm|20.0|-1.0|25.0
     *   END
     *
     * File format built sequentially:
     * - Writes each course block to file in order
     * - Multiple courses concatenated in single file
     * - Each course ends with END marker for clean separation
     *
     * Robustness features:
     * - Automatically creates parent directory if missing (via ensureParentDirectoryExists)
     * - Uses try-with-resources to guarantee FileWriter closure
     * - Overwrites entire file atomically (FileWriter Constructor behavior)
     * - Ensures no partial/corrupted data persists from previous saves
     *
     * Design consideration:
     * Atomic overwrite prevents scenarios where crash during write leaves file
     * in corrupted state (old data partially overwritten). FileWriter replaces
     * entire file contents, so either old or new data persists, never both.
     *
     * @param courseList the CourseList containing all courses to save
     * @throws CourseException if file cannot be written or parent directory creation fails
     */
    public void save(CourseList courseList) throws CourseException {
        ensureParentDirectoryExists();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Course course : courseList.getAll()) {
                writer.write(course.encode());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new CourseException("Could not save courses to disk.");
        }
    }

    /**
     * Ensures the parent directory exists before reading or writing course files.
     * Called defensively before every load() and save() operation.
     *
     * Two-tier robustness strategy:
     * 1. Null check: parent may be null for files in working directory
     * 2. Existence check: only creates if directory missing (avoid redundant calls)
     * 3. Success check: verifies mkdirs() succeeded before proceeding
     *
     * Usage examples:
     * - First run: no data directory exists, this creates it automatically
     * - User deletes directory: recreated on next save/load operation
     * - Permission denied: throws CourseException with clear error message
     *
     * Benefits of defensive directory creation:
     * - Users don't need to manually create data folder
     * - Gracefully handles directory deletion/corruption scenarios
     * - Prevents confusing IOException about missing directory
     *
     * @throws CourseException if parent directory creation fails or insufficient permissions
     */
    private void ensureParentDirectoryExists() throws CourseException {
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            boolean success = parent.mkdirs();
            if (!success) {
                throw new CourseException("Could not create data folder.");
            }
        }
    }
}

