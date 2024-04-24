package view;

import java.util.List;

import model.Event;

/**
 * This interface represents the view for the planner system.
 * The view is responsible for displaying the schedule and handling user input.
 * The controller interacts with the view through this interface.
 */
public interface IPlannerView {
  /**
   * Displays or updates the schedule on the view.
   * @param events A list of events to display.
   */
  void updateSchedule(List<Event> events);

  /**
   * Sets the listener for user actions within the view.
   * The controller implements the listener interface and registers itself with the view.
   * @param listener The listener for user actions.
   */
  void setListener(IPlannerViewListener listener);

  /**
   * Displays an error message to the user.
   * @param message The error message to display.
   */
  void showError(String message);

  /**
   * Makes the view visible or hides it.
   * @param visible true to make the view visible; false to hide it.
   */
  void setVisible(boolean visible);
}
