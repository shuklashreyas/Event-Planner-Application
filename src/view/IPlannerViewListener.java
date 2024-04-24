package view;

import model.Event;
import model.User;

/**
 * This interface represents the listener for the planner view.
 * The controller implements this interface to handle user actions.
 */
public interface IPlannerViewListener {
  /**
   * Called when the user creates a new event.
   *
   * @param event the event to create
   * @param userId the user ID of the event creator
   */
  void onEventCreate(Event event, String userId);

  /**
   * Called when the user loads a schedule from a file.
   *
   * @param filePath the file path to load the schedule from
   * @param user the user loading the schedule
   */
  void onScheduleLoad(String filePath, User user);

}
