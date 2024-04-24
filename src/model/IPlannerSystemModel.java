package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents the model for the planner system.
 * This model has various functionalities such as uploading and saving schedules,
 * selecting a user, creating, modifying, and removing events, and auto-scheduling events.
 * It also has the functionality to see events occurring at a given time for a given user.
 * This model is used by the controller to interact with the view.
 */
public interface IPlannerSystemModel extends IReadOnlyModel {

  /**
   * Upload an XML file representing a single user’s schedule.
   *
   * @param xmlFilePath the path to the XML file
   * @param user        the user to upload the schedule for
   * @return true if the schedule was uploaded successfully, false otherwise
   * @throws IllegalArgumentException if the user is null or the XML file is invalid
   * @throws IllegalStateException    if there is an error reading the XML file
   */
  boolean uploadSchedule(String xmlFilePath, User user);

  /**
   * Save each user’s schedule to an XML file.
   *
   * @param xmlFilePath the path to the XML file
   * @param user        the user to save the schedule for
   * @return true if the schedule was saved successfully, false otherwise
   * @throws IllegalArgumentException if the user is null or the XML file is invalid
   * @throws IllegalStateException    if there is an error writing the XML file
   */
  boolean saveSchedule(String xmlFilePath, User user);

  /**
   * Select one of the users to display their schedule.
   *
   * @param user the user to select
   * @throws IllegalArgumentException if the user is null
   */
  void selectUser(User user);

  /**
   * Create an event for a user.
   *
   * @param user  the user to create the event for
   * @param event the event to create
   * @return true if the event was created successfully, false otherwise
   * @throws IllegalArgumentException if the event is null or the user is null
   * @throws IllegalStateException    if there is an error creating the event
   * @throws IllegalStateException    if the event conflicts with an existing event
   */
  boolean createEvent(User user, Event event);

  /**
   * Modify an event for a user.
   *
   * @param user          the user to modify the event for
   * @param originalEvent the original event to modify
   * @param updatedEvent  the updated event
   * @return true if the event was modified successfully, false otherwise
   * @throws IllegalArgumentException if the user, originalEvent, or updatedEvent is null
   * @throws IllegalArgumentException if the originalEvent and updatedEvent are the same
   * @throws IllegalStateException    if the user does not exist in the system
   * @throws IllegalStateException    if there is an error modifying the event
   * @throws IllegalStateException    if the updated event conflicts with an existing event
   */
  boolean modifyEvent(User user, Event originalEvent, Event updatedEvent);

  /**
   * Remove an event for a user.
   *
   * @param user  the user to remove the event for
   * @param event the event to remove
   * @return true if the event was removed successfully, false otherwise
   * @throws IllegalArgumentException if the user or event is null
   * @throws IllegalStateException    if there is an error removing the event
   * @throws IllegalStateException    if the event does not exist
   */
  boolean removeEvent(User user, Event event);

  /**
   * Have the program automatically schedule an event on some users’
   * schedules at some time if possible.
   *
   * @param user  the user to schedule the event for
   * @param event the event to schedule
   * @return true if the event was scheduled successfully, false otherwise
   * @throws IllegalArgumentException if the user or event is null
   * @throws IllegalStateException    if there is an error scheduling the event
   * @throws IllegalStateException    if the event conflicts with an existing event
   */
  boolean autoSchedule(User user, Event event);

  /**
   * See events occurring at a given time for the given user.
   *
   * @param user the user to see the events for
   * @param time the time to see the events for
   * @return a list of events occurring at the given time for the given user
   * @throws IllegalArgumentException if the user is null or the time is null
   * @throws IllegalStateException    if there is an error seeing the events
   */
  List<Event> seeEvents(User user, LocalDateTime time);

  /**
   * Get all users in the system as a list.
   *
   * @return a list of all users in the system
   */
  List<User> getUsers();

  /**
   * Add a user to the system.
   *
   * @param user the user to add
   */
  void addUser(User user);

  /**
   * Get a user from the system.
   *
   * @param userId the id of the user to get
   * @return the user with the given id
   */
  User getUser(String userId);

  /**
   * Select and modify a user's schedule.
   *
   * @param userId the id of the user to select and modify
   */
  void selectAndModifyUserSchedule(String userId);

  /**
   * Create an event based on the scheduling strategy.
   * Since there are multiple strategies.
   * This method will create an event based on the strategy.
   *
   * @param user  The user for whom the event is created
   * @param event The event to be created
   */
  void createEventBasedOnStrategy(User user, Event event);


}