package seedu.duke.tasklist;

import seedu.duke.calender.Calendar;
import seedu.duke.task.Event;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EventList extends TaskList<Event>{

    public EventList() {
        super();
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < tasks.size(); i++) {
            result = result + (i + 1) + ". " + (tasks.get(i).toString()) + System.lineSeparator();
        }
        return result;
    }

    public void clearAll() {
        tasks.clear();
    }

    public void sortByDate() {
        tasks.sort(Comparator
                .comparing(Event::getFrom)
                .thenComparing(Event::getTo));
    }

    public Event getLatest() {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(tasks.size() - 1);
    }

    public void addRecurringWeeklyEvent(Event event, Calendar calendar) {
        LocalDateTime boundaryDateTime = event.getFrom().plusMonths(1);
        LocalDateTime currentFromDateTime = event.getFrom();
        LocalDateTime currentToDateTime = event.getTo();
        int recurringGroupId = event.getRecurringGroupId();
        String eventDescription = event.getDescription();

        while (currentFromDateTime.isBefore(boundaryDateTime)){
            Event newEvent = new Event(eventDescription,currentFromDateTime,
                    currentToDateTime,true,recurringGroupId);
            tasks.add(newEvent);
            calendar.registerTask(newEvent);
            currentFromDateTime = currentFromDateTime.plusDays(7);
            currentToDateTime = currentToDateTime.plusDays(7);

        }
    }

    public void sortByDay() {
        Comparator<Event> dayOfWeek = (d1,d2)
                -> Integer.compare(d1.getFrom().getDayOfWeek().getValue(),d2.getFrom().getDayOfWeek().getValue());
        tasks.sort(dayOfWeek);
    }
}
