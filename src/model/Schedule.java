package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a schedule in the calendar system.
 * A schedule has a list of events.
 * We can add and remove events from the schedule.
 * We can also get the list of events in the schedule.
 */
public class Schedule implements ISchedule {
  private List<Event> events;

  /**
   * Constructs a new Schedule with an empty list of events.
   */
  public Schedule() {
    this.events = new ArrayList<>();
  }

  @Override
  public void addEvent(Event event) {
    events.add(event);
  }

  @Override
  public boolean removeEvent(Event event) {
    return events.remove(event);
  }

  @Override
  public List<Event> getEvents() {
    return new ArrayList<>(events);
  }

  @Override
  public boolean hasEventConflict(Event event) {
    for (Event e : events) {
      if (e.conflictsWith(event)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isAvailable(LocalDateTime startSearch, LocalDateTime endSearchTime) {
    for (Event e : events) {
      if (e.getStartTime().isBefore(endSearchTime) && e.getEndTime().isAfter(startSearch)) {
        return false;
      }
    }
    return true;
  }
}