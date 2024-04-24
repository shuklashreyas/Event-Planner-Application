package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface represents an event in the calendar system.
 */
public interface IEvent {

  /**
   * Checks if this event conflicts with another event.
   *
   * @param otherEvent the other event to check for conflicts with
   * @return true if the events conflict, false otherwise
   */
  boolean checkForConflict(Event otherEvent);

  /**
   * Gets the name of the event.
   *
   * @return the name of the event
   */
  String getName();

  /**
   * Sets the name of the event.
   *
   * @param name the new name of the event
   */
  void setName(String name);

  /**
   * Gets the start time of the event.
   *
   * @return the start time of the event
   */
  LocalDateTime getStartTime();

  /**
   * Sets the start time of the event.
   *
   * @param startTime the new start time of the event
   */
  void setStartTime(LocalDateTime startTime);

  /**
   * Gets the end time of the event.
   *
   * @return the end time of the event
   */
  LocalDateTime getEndTime();

  /**
   * Sets the end time of the event.
   *
   * @param endTime the new end time of the event
   */
  void setEndTime(LocalDateTime endTime);

  /**
   * Gets the location of the event.
   *
   * @return the location of the event
   */
  String getLocation();

  /**
   * Sets the location of the event.
   *
   * @param location the new location of the event
   */
  void setLocation(String location);

  /**
   * Checks if the event is online.
   *
   * @return true if the event is online, false otherwise
   */
  boolean isOnline();

  /**
   * Sets whether the event is online.
   *
   * @param isOnline whether the event is online
   */
  void setOnline(boolean isOnline);

  /**
   * Gets the invitees of the event.
   *
   * @return the invitees of the event
   */
  List<String> getInvitees();

  /**
   * Sets the invitees of the event.
   *
   * @param invitees the new invitees of the event
   */
  void setInvitees(List<String> invitees);

  /**
   * Gets the duration of the event.
   *
   * @return the duration of the event
   * @throws IllegalStateException if the start time is after the end time
   */
  long getDuration();

  /**
   * Checks if this event conflicts with another event.
   *
   * @param event the other event to check for conflicts with
   * @return true if the events conflict, false otherwise
   */

  boolean conflictsWith(Event event);

  /**
   * Gets the start time of the event.
   *
   * @return the start time of the event
   */
  LocalDateTime getStart();

  /**
   * Gets the end time of the event.
   *
   * @return the end time of the event
   */
  LocalDateTime getEnd();

  /**
   * Gets the duration of the event.
   *
   * @param startSearch the start time to search for
   */
  void setTime(LocalDateTime startSearch);
}
