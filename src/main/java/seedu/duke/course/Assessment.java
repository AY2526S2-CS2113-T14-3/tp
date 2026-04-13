package seedu.duke.course;

/**
 * Represents one assessment component inside a course.
 * Example:
 * Finals, 40% weightage, scored 85 out of 100
 * MICHAEL:
 * - Your parser/manager will create Assessment objects when the user types:
 * add-assessment CS2113 /n Finals /w 40 /ms 100
 * - Your score command will later call recordScore(...)
 */

public class Assessment {

    private static final double NOT_GRADED = -1;
    private static final double ZERO_SCORE = 0;

    private final String name;
    private final double weightage;
    private double scoreObtained;
    private final double maxScore;


    public Assessment(String name, double weightage, double maxScore) {
        this.name = name;
        this.weightage = weightage;
        this.maxScore = maxScore;
        this.scoreObtained = NOT_GRADED;
    }
    //Creates an assessment that has not been graded yet.
    //scoreObtained = NOT_GRADED means "not graded".

    public Assessment(String name, double weightage, double scoreObtained, double maxScore) {
        this.name = name;
        this.weightage = weightage;
        this.scoreObtained = scoreObtained;
        this.maxScore = maxScore;
    }
    //Creates an assessment with an already recorded score.

    public String getName() {
        return name;
    }

    public double getWeightage() {
        return weightage;
    }

    public double getScoreObtained() {
        return scoreObtained;
    }

    public double getMaxScore() {
        return maxScore;
    }


    /**
     * Checks if this assessment has been graded.
     * An assessment is considered graded if scoreObtained >= 0.
     * Uses NOT_GRADED (-1) as sentinel value to distinguish between
     * "not yet graded" and "graded with 0 points".
     *
     * @return true if a score has been recorded, false if still pending
     */
    public boolean isGraded() {
        return scoreObtained >= 0;
    }

    /**
     * Records the score obtained for this assessment component.
     * Updates the graded status automatically. Validates score >= 0 and <= maxScore.
     *
     * @param score the score obtained by the student
     * @throws IllegalArgumentException if score is negative or exceeds maxScore
     */
    public void recordScore(double score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative: " + score);
        }
        if (score > maxScore) {
            throw new IllegalArgumentException("Score " + score + " exceeds maximum " + maxScore);
        }
        this.scoreObtained = score;
    }

    /**
     * Resets this assessment back to ungraded state.
     * Useful for undoing a score command or correcting data entry.
     * After calling this, isGraded() returns false and getWeightedScore() returns 0.
     *
     * This method is typically called by the Undo feature when reversing a score command.
     */
    //@@author michaelshyam1
    public void resetScore() {
        this.scoreObtained = NOT_GRADED;
    }

    /**
     * Calculates the weighted score for this assessment.
     * Formula: (scoreObtained / maxScore) * 100 * weightage / 100
     *
     * Returns 0 if assessment is not yet graded (scoreObtained == NOT_GRADED).
     * This prevents ungraded assessments from skewing the total weighted score.
     *
     * @return weighted score percentage contribution to overall grade
     */
    public double getWeightedScore() {
        if (!isGraded()) {
            return ZERO_SCORE;
        }
        return (scoreObtained / maxScore) * 100 * (weightage / 100);
    }


    /**
     * Encodes this assessment into a single line for persistent storage.
     *
     * Format: name|weightage|scoreObtained|maxScore
     * Example: "Finals|40.0|85.0|100.0"
     *
     * Uses pipe-delimited format for clean line-based file storage.
     * Preserves all assessment state including sentinel NOT_GRADED value (-1).
     *
     * @return encoded string representation ready for file output
     */
    public String encode() {
        return name + "|" + weightage + "|" + scoreObtained + "|" + maxScore;
    }

    /**
     * Decodes one saved line back into an Assessment object.
     * Inverse of encode() method - used during course loading from disk.
     *
     * Expected format: name|weightage|scoreObtained|maxScore
     * Example input: "Finals|40.0|85.0|100.0"
     * Example input: "Midterm|20.0|-1.0|25.0" (NOT_GRADED)
     *
     * Robust error handling:
     * - Returns null if fewer than 4 parts (malformed line)
     * - Returns null if any numeric field cannot be parsed
     * - Silently continues on error to prevent cascading failures
     *
     * @param encoded the pipe-delimited assessment line from storage
     * @return Assessment object if valid, null if line is malformed
     */
    public static Assessment decode(String encoded) {
        String[] parts = encoded.split("\\|");
        if (parts.length != 4) {
            return null;
        }

        try {
            String name = parts[0].trim();
            double weightage = Double.parseDouble(parts[1]);
            double scoreObtained = Double.parseDouble(parts[2]);
            double maxScore = Double.parseDouble(parts[3]);
            return new Assessment(name, weightage, scoreObtained, maxScore);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String scoreText = isGraded()
                ? String.format("%.1f / %.1f", scoreObtained, maxScore)
                : "Not graded";

        return String.format("%s (weight: %.1f%%, score: %s)", name, weightage, scoreText);
    }
}

