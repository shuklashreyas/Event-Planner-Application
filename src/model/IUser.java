package model;

import java.util.List;

/**
 * This interface represents a user in the calendar system.
 */
public interface IUser {

  /**
   * Gets the ID of the user.
   *
   * @return the ID of the user
   */
  String getId();

  /**
   * Gets the name of the user.
   *
   * @return the name of the user
   */
  String getName();

  /**
   * Gets the schedule of the user.
   *
   * @return the schedule of the user
   */
  Schedule getSchedule();

  /**
   * Gets the events of the user.
   *
   * @return the events of the user
   */
  List<Event> getEvents();

  User getUser(Event event);
}
