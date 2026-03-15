package seedu.duke.task;

import java.time.LocalDateTime;

/**
 * A functional contract for tasks that are associated with a specific point in time.
 * Classes implementing this interface can be managed by the {@code Calendar} class
 * for chronological sorting, range-based display, and date-specific queries.
 */
public interface Timed {
    LocalDateTime getDate();
}
