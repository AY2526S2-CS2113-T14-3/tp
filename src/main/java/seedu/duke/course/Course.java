package seedu.duke.course;

import java.util.ArrayList;

/**
 * Represents one university course, e.g. CS2113.
 * Each course contains a list of assessments.
 */

public class Course {

    private final String courseCode;
    private final ArrayList<Assessment> assessments;

    public Course(String courseCode) {
        this.courseCode = courseCode.toUpperCase().trim();
        this.assessments = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }

    /**
     * Adds a new assessment to this course.
     * Assessments are stored in insertion order and contribute to overall grade calculation.
     * No validation is performed here; validation should be done at CourseManager level.
     *
     * @param assessment the assessment to add (must not be null)
     */
    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
    }

    /**
     * Checks if an assessment with the given name exists in this course.
     * Search is case-insensitive to match typical user behavior.
     *
     * @param name the assessment name to search for
     * @return true if an assessment with this name exists, false otherwise
     */
    public boolean hasAssessment(String name) {
        return getAssessment(name) != null;
    }

    /**
     * Retrieves an assessment by name with case-insensitive matching.
     * Returns the first assessment that matches the given name.
     *
     * Critical design note: Returns null if no match found. Calling code should
     * check for null before using returned assessment to avoid NullPointerException.
     *
     * @param name the assessment name to search for
     * @return the matching Assessment object, or null if not found
     */
    public Assessment getAssessment(String name) {
        for (Assessment assessment : assessments) {
            if (assessment.getName().equalsIgnoreCase(name)) {
                return assessment;
            }
        }
        return null;
    }

    /**
     * Removes an assessment from this course by name with case-insensitive matching.
     * Successfully removing an assessment returns true; if the assessment does not exist,
     * returns false and the course state is unchanged.
     *
     * Design consideration: Removing assessments recalculates weightage totals.
     * If total weightage < 100, the course becomes incomplete/invalid.
     * Validation of new total weightage should be performed by CourseManager.
     *
     * @param name the assessment name to remove
     * @return true if assessment was found and removed, false if assessment not found
     */
    public boolean removeAssessment(String name) {
        Assessment assessment = getAssessment(name);
        if (assessment == null) {
            return false;
        }
        assessments.remove(assessment);
        return true;
    }

    public int getAssessmentCount() {
        return assessments.size();
    }

    /**
     * Calculates the sum of all weighted scores from graded assessments.
     * Weighted score for each assessment is calculated as:
     * (scoreObtained / maxScore) * weightage (as percentage)
     *
     * Example: Finals (40% weight, 85/100): contributes 40 * 0.85 = 34 points
     *         Midterm (20% weight, not graded): contributes 0
     *         Total: 34+0 = 34 points
     *
     * Critical design: Ungraded assessments contribute 0 (by design of Assessment.getWeightedScore())
     * This prevents incomplete work from artificially reducing the grade.
     *
     * @return sum of weighted scores from all assessments (includes 0 for ungraded)
     */
    public double getTotalWeightedScore() {
        double total = 0;
        for (Assessment assessment : assessments) {
            total += assessment.getWeightedScore();
        }
        return total;
    }

    /**
     * Calculates the sum of all assessment weightages in this course.
     * Used to validate course configuration and compute grade percentages.
     *
     * Valid course configuration: Total weightage = 100%
     * Example: Finals (40%) + Midterm (20%) + Lab (15%) + Assignment (25%) = 100%
     *
     * Note: getTotalWeightage() and getGradedWeightage() may differ if some
     * assessments are not yet graded.
     *
     * @return sum of all assessment weightages (typically 100 for a complete course)
     */
    public double getTotalWeightage() {
        double total = 0;
        for (Assessment assessment : assessments) {
            total += assessment.getWeightage();
        }
        return total;
    }

    /**
     * Calculates the sum of weightages for only graded (scored) assessments.
     * Used to compute current grade percentage relative to completed work only.
     * Formula: totalWeightedScore / gradedWeightage * 100 = current percentage
     *
     * Example: If Finals (40%) and Midterm (20%) are graded but Lab (15%) is pending:
     * gradedWeightage = 40 + 20 = 60 (representing completed assessment weight)
     *
     * Design rationale: By comparing only against graded work, students receive
     * accurate interim grades and aren't penalized for incomplete assessments.
     *
     * @return sum of weightages for graded assessments only (0 if no assessments graded)
     */
    public double getGradedWeightage() {
        double total = 0;
        for (Assessment assessment : assessments) {
            if (assessment.isGraded()) {
                total += assessment.getWeightage();
            }
        }
        return total;
    }

    /**
     * Encodes this course and all its assessments into a block string for file persistence.
     * Uses a custom text-based format with pipe delimiters for assessment data.
     *
     * File format structure:
     * COURSE:CS2113
     * Finals|40.0|85.0|100.0
     * Midterm|20.0|-1.0|25.0
     * Assignment|25.0|20.0|20.0
     * END
     *
     * Key format details:
     * - Starts with COURSE: header followed by course code
     * - Each assessment encoded as: name|weightage|scoreObtained|maxScore
     * - Sentinel value -1.0 in scoreObtained field indicates ungraded assessment
     * - Ends with END marker to cleanly separate multiple courses
     *
     * Robustness: The END marker allows CourseStorage to parse multiple courses
     * sequentially without line count tracking.
     *
     * @return encoded block string ready for file output
     */
    public String encode() {
        StringBuilder sb = new StringBuilder();
        sb.append("COURSE:").append(courseCode).append(System.lineSeparator());

        for (Assessment assessment : assessments) {
            sb.append(assessment.encode()).append(System.lineSeparator());
        }

        sb.append("END").append(System.lineSeparator());
        return sb.toString();
    }


    @Override
    public String toString() {
        return courseCode + " (" + assessments.size() + " assessment(s))";
    }

    /**
     * Calculates the current percentage grade for this course based on graded assessments only.
     *
     * Formula: totalWeightedScore / gradedWeightage * 100
     *
     * Detailed calculation example:
     * - Finals (40% weight, scored 85/100): weighted contribution = 40 * 0.85 = 34 points
     * - Midterm (20% weight, scored 20/25): weighted contribution = 20 * 0.80 = 16 points  
     * - Lab (15% weight, NOT graded): weighted contribution = 0 (excluded from denominator)
     * - Current grade = (34 + 16) / (40 + 20) * 100 = 50 / 60 * 100 = 83.33%
     *
     * Critical design rationale:
     * By using gradedWeightage as denominator, we only compare against completed work.
     * This prevents ungraded assessments from artificially depressing the current grade,
     * allowing students to see their true performance on completed work.
     *
     * Edge case handling: If no assessments are graded, returns 0 (course not yet started).
     *
     * @return current grade percentage (0-100), or 0 if no assessments graded yet
     */
    public double getCurrentGradePercentage() {
        double gradedWeightage = getGradedWeightage();
        if (gradedWeightage == 0) {
            return 0;
        }
        return (getTotalWeightedScore() / gradedWeightage) * 100;
    }

    /**
     * Determines if all assessments for this course have been graded (scored).
     * Useful for checking course completion status and determining if final grade is available.
     *
     * Business logic: A course is fully graded when every assessment in the course
     * has a recorded score (not in the ungraded sentinel state).
     *
     * Design considerations:
     * - Empty courses (0 assessments) return false (course cannot be complete)
     * - Ungraded == scoreObtained == -1.0 (sentinel value from Assessment class)
     * - This check is often used before retrieving final grade percentage
     *
     * Example usage:
     *   if (course.isFullyGraded()) {
     *       print(course.getCurrentGradePercentage()); // Final grade ready
     *   } else {
     *       print("Waiting for " + remainingCount + " assessments");
     *   }
     *
     * @return true if all assessments have recorded scores, false if any are pending or course empty
     */
    public boolean isFullyGraded() {
        if (assessments.isEmpty()) {
            return false;
        }
        for (Assessment assessment : assessments) {
            if (!assessment.isGraded()) {
                return false;
            }
        }
        return true;
    }
}

