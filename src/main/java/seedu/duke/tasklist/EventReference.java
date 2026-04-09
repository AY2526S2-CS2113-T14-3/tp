package seedu.duke.tasklist;


import seedu.duke.task.Event;

/**
 * Represents the reference for {@link Event} based on the categoryIndex and the eventIndex.
 */
public class EventReference {
    public final int categoryIndex;
    public final int eventIndex;

    public EventReference(int categoryIndex, int eventIndex) {
        this.categoryIndex = categoryIndex;
        this.eventIndex = eventIndex;
    }
}
