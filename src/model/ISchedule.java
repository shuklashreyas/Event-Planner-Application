package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface represents a schedule in the calendar system.
 */
public interface ISchedule {

  /**
   * Adds a new event to the schedule.
   *
   * @param event the event that is being added
   */
  public void addEvent(Event event);

  /**
   * Removes a specific event from the schedule.
   *
   * @param event the event to be removed
   * @return true if the event was successfully removed.
   */
  public boolean removeEvent(Event event);

  /**
   * Retrieves the current list of events in the Schedule.
   *
   * @return A new list of the events currently in the Schedule.
   */
  public List<Event> getEvents();

  public boolean hasEventConflict(Event event);

  /**
   * Check if the schedule is available for a given time range.
   *
   * @param startSearch   the start time to search for
   * @param endSearchTime the end time to search for
   * @return true if the schedule is available, false otherwise
   */

  public boolean isAvailable(LocalDateTime startSearch, LocalDateTime endSearchTime);

}
