package seedu.duke.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a deadline task.
 * Deadline task includes a description and a date and/or time by which
 * the task must be completed. It implements the {@link Timed} interface to
 * allow for chronological sorting and calendar integration.
 */
public class Deadline extends Task implements Timed {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Parses a string input into a {@code LocalDateTime} and ensures the year is 2026 or later.
     * If only a date is provided, the time defaults to 23:59 (end of day).
     *
     * @param input The date or date-time string (e.g., "2026-12-31" or "2026-12-31 1800").
     * @return A {@code LocalDateTime} representation of the input.
     * @throws DateTimeParseException If the input does not match expected formats or year is before 2026.
     */
    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        assert input != null : "Date input string should not be null";
        String trimmedInput = input.trim();
        assert !trimmedInput.isEmpty() : "Date input string should not be empty";

        LocalDateTime parsedDate;

        try {
            DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            parsedDate = LocalDateTime.parse(trimmedInput, fullFormatter);
        } catch (DateTimeParseException e) {
            DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(trimmedInput, dateOnlyFormatter);
            parsedDate = date.atTime(23, 59);
        }

        if (parsedDate.getYear() < 2026) {
            throw new DateTimeParseException("Date must be in the year 2026 or later.",
                    trimmedInput, 0);
        }

        return parsedDate;
    }

    /**
     * Gets the deadline date and time.
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toFileFormat() {
        DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return String.format("D | %d | %s | %s", (isDone ? 1 : 0), description, by.format(storageFormatter));
    }

    @Override
    public String toString() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return "[D]" + super.toString() + " (by: " + by.format(displayFormatter) + ")";
    }

    /**
     * Retrieves the date/time associated with this task for calendar synchronization.
     * This uses the {@link Timed} interface.
     *
     * @return The deadline (by) date and time.
     */
    @Override
    public LocalDateTime getDate() {
        return by; // This satisfies the Timed interface
    }
}
