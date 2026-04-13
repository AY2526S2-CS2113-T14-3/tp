package seedu.duke.course;

import seedu.duke.exception.CourseException;
import seedu.duke.storage.CourseStorage;
import seedu.duke.ui.CourseUi;

public class CourseManager {
    private static final double MAX_ALLOWED_SCORE = 10000;
    private static final double MAX_WEIGHTAGE = 100;
    private static final double MIN_WEIGHTAGE = 0;
    private static final double MIN_SCORE = 0;

    //stores all courses
    private final CourseList courseList;
    //handles saving and loading courses
    private final CourseStorage courseStorage;

    public CourseManager(String filePath) throws CourseException {
        this.courseList = new CourseList();
        this.courseStorage = new CourseStorage(filePath);
        this.courseList.setAll(courseStorage.load());
    }

    public CourseList getCourseList() {
        return courseList;
    }

    /**
     * Creates and adds a new course to the manager.
     * Normalizes course code to uppercase and validates it hasn't been added yet.
     *
     * Validation rules:
     * - Course code must not be null or empty (after trimming)
     * - Course code must not already exist (case-insensitive duplicate check)
     *
     * Design note: Course additions are persisted immediately to CourseStorage.
     * This ensures data is preserved even if the application crashes.
     *
     * @param courseCode the course identifier (e.g., "CS2113", "ST2334")
     * @return confirmation message with normalized course code
     * @throws CourseException if code is empty, null, or course already exists
     */
    public String addCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();

        if (courseList.contains(normalizedCode)) {
            throw new CourseException("Course already exists: " + normalizedCode);
        }

        courseList.add(new Course(normalizedCode));
        save();
        return "Added course: " + normalizedCode;
    }

    public String listCourses() {
        return CourseUi.formatCourseList(courseList);
    }

    public String deleteCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();

        if (!courseList.remove(normalizedCode)) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        save();
        return "Deleted course: " + normalizedCode;
    }

    /**
     * Validates and adds a new assessment to the specified course.
     * Enforces critical constraints to maintain valid course grading configuration.
     *
     * Validation rules enforced:
     * - Course code must be valid and course must exist
     * - Assessment name must be unique within the course
     * - Weightage must be in range (0, 100]
     * - MaxScore must be in range (0, 10000]
     * - Total weightage after addition cannot exceed 100%
     *
     * Example valid sequence:
     *   addAssessment("CS2113", "Finals", 40.0, 100.0)      // OK: total 40%
     *   addAssessment("CS2113", "Midterm", 30.0, 100.0)     // OK: total 70%
     *   addAssessment("CS2113", "Lab", 15.0, 50.0)          // OK: total 85%
     *   addAssessment("CS2113", "Assignment", 20.0, 50.0)   // ERROR: total 105% > 100%
     *
     * Design rationale for MAX_WEIGHTAGE=100%:
     * Prevents configuration where assessments account for more than the full grade,
     * which would create impossible grading scenarios.
     *
     * Design rationale for MAX_ALLOWED_SCORE=10000:
     * Allows flexible scoring (0-100 points, 0-1000 points, etc.) while preventing
     * unreasonable score values that might indicate data error or overflow.
     *
     * @param courseCode the course identifier (will be normalized to uppercase)
     * @param name assessment name (must be unique in course; examples: "Finals", "Midterm")
     * @param weightage percentage weight in final grade (standard range 0-100)
     * @param maxScore maximum possible score for this assessment (standard range 0-100 or 0-50)
     * @return success message with assessment name and weightage
     * @throws CourseException if course not found, name already exists, weightage/score invalid,
     *         or total weightage would exceed 100%
     */
    public String addAssessment(String courseCode, String name, double weightage, double maxScore) 
            throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty");
        }
        if (weightage <= 0 || weightage > MAX_WEIGHTAGE) {
            throw new CourseException("Weightage must be between 0 and " + MAX_WEIGHTAGE);
        }
        if (maxScore <= 0) {
            throw new CourseException("Maximum score must be positive");
        }
        if (maxScore > MAX_ALLOWED_SCORE) {
            throw new CourseException("Maximum score cannot exceed " + MAX_ALLOWED_SCORE + ".");
        }
        
        Course course = courseList.get(courseCode);
        if (course == null) {
            throw new CourseException("Course not found: " + courseCode);
        }
        
        if (course.hasAssessment(name)) {
            throw new CourseException("Assessment already exists in " + courseCode + ": " + name);
        }
        
        // Check if adding this assessment would exceed 100% total weightage
        double newTotal = course.getTotalWeightage() + weightage;
        if (newTotal > MAX_WEIGHTAGE) {
            throw new CourseException("Total assessment weightage cannot exceed " + MAX_WEIGHTAGE + "%.");
        }
        
        Assessment assessment = new Assessment(name, weightage, maxScore);
        course.addAssessment(assessment);
        courseStorage.save(courseList);
        return "Added assessment: " + name + " (" + weightage + "%)";
    }

    /**
     * Records a score for an assessment and updates the course's weighted grade.
     * Validates score is within bounds before delegating to Assessment.recordScore().
     *
     * Validation rules:
     * - Course code must be valid and course must exist
     * - Assessment name must be valid and exist in the course
     * - Score must be non-negative
     * - Score must not exceed the assessment's maximum possible score
     *
     * Critical design: Input validation is performed here (range checks),
     * while Assessment.recordScore() performs its own validation for defensive programming.
     * This two-layer validation catches errors at multiple levels.
     *
     * Side effects: Persists changes to CourseStorage immediately for data preservation.
     *
     * @param courseCode the course identifier (case-insensitive, will be normalized)
     * @param assessmentName the assessment name within the course (case-insensitive)
     * @param score the numerical score achieved (range 0 to assessment's maxScore)
     * @return confirmation message with assessment name and score details
     * @throws CourseException if course/assessment not found, score invalid, or score exceeds maximum
     */
    public String recordScore(String courseCode, String assessmentName, double score) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }
        if (assessmentName == null || assessmentName.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty.");
        }
        if (score < 0) {
            throw new CourseException("Score cannot be negative.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        String normalizedAssessmentName = assessmentName.trim();

        Course course = courseList.get(normalizedCode);
        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        Assessment assessment = course.getAssessment(normalizedAssessmentName);
        if (assessment == null) {
            throw new CourseException("Assessment not found in " + normalizedCode + ": "
                    + normalizedAssessmentName);
        }

        if (score > assessment.getMaxScore()) {
            throw new CourseException("Score cannot exceed maximum score of " + assessment.getMaxScore() + ".");
        }

        assessment.recordScore(score);
        save();

        return "Recorded score for " + normalizedAssessmentName + " in " + normalizedCode
                + ": " + score + "/" + assessment.getMaxScore();
    }

    /**
     * Removes an assessment from a course and updates the course's weightage distribution.
     * Validates course and assessment existence before deletion.
     *
     * Critical design consideration:
     * After deletion, the course's total weightage may fall below 100%.
     * This is permitted to allow flexible course management (e.g., removing cancelled exams).
     * However, the course becomes invalid for grading until weightage is restored to 100%.
     * Validation of new total weightage is the responsibility of CourseManager clients.
     *
     * @param courseCode the course identifier (case-insensitive, will be normalized)
     * @param assessmentName the assessment to remove from the course
     * @return confirmation message with removed assessment details
     * @throws CourseException if course or assessment not found
     */
    public String deleteAssessment(String courseCode, String assessmentName) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }
        if (assessmentName == null || assessmentName.trim().isEmpty()) {
            throw new CourseException("Assessment name cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        String normalizedAssessmentName = assessmentName.trim();

        Course course = courseList.get(normalizedCode);
        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        if (!course.removeAssessment(normalizedAssessmentName)) {
            throw new CourseException("Assessment not found in " + normalizedCode + ": "
                    + normalizedAssessmentName);
        }

        save();
        return "Deleted assessment " + normalizedAssessmentName + " from " + normalizedCode;
    }

    /**
     * Retrieves and displays detailed information for a single course.
     * Formats the course's assessment list and current grade information for display.
     *
     * Information displayed includes:
     * - Course code
     * - List of all assessments with their names, weightages, and scores
     * - Total weightage and current grade percentage (if all assessments graded)
     *
     * @param courseCode the course identifier to view (case-insensitive)
     * @return formatted string with course details ready for display
     * @throws CourseException if course code is empty or course not found
     */
    public String viewCourse(String courseCode) throws CourseException {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new CourseException("Course code cannot be empty.");
        }

        String normalizedCode = courseCode.toUpperCase().trim();
        Course course = courseList.get(normalizedCode);

        if (course == null) {
            throw new CourseException("Course not found: " + normalizedCode);
        }

        return CourseUi.formatCourse(course);
    }

    private void save() throws CourseException {
        courseStorage.save(courseList);
    }
}
