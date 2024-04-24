package view;

import java.util.List;

import model.User;

/**
 * The interface for the planner view.
 * This is the view either in text or graphical form.
 */
public interface IPlannerViewTextual {
  /**
   * Display each user's schedule with the correct formatting.
   * Either text or graphical.
   * @param users the list of users to display
   */
  void displayUsersSchedules(List<User> users);
}
