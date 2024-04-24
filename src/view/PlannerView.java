package view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import model.Event;

/**
 * The view for the planner.
 */
public class PlannerView {

  private IPlannerViewListener listener;

  /**
   * Sets the listener for the view.
   *
   * @param listener the listener to set
   */
  public void setListener(IPlannerViewListener listener) {
    this.listener = listener;
  }

  /**
   * Updates the schedule on the view.
   *
   * @param events the list of events to display
   */
  public void updateSchedule(List<Event> events) {
    System.out.println("Updated Schedule:");
    for (Event event : events) {
      System.out.println(event);
    }
  }

  /**
   * Displays an error message to the user.
   *
   * @param b the error message to display
   */
  public void setVisible(boolean b) {
    if (b) {
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("Enter command (create/load/exit):");
        String command = scanner.nextLine();
        if ("exit".equalsIgnoreCase(command)) {
          break;
        }
        if ("create".equalsIgnoreCase(command) && listener != null) {
          // give random event
          LocalDateTime now = LocalDateTime.now();
          LocalDateTime eventTime = now.plusDays(1);

          Event event = new Event("Event", now, eventTime,
                  "Home", true, List.of("user1"), "user1");
          listener.onEventCreate(event, "user1");
        } else if ("load".equalsIgnoreCase(command) && listener != null) {
          listener.onScheduleLoad("schedule.txt", null);
        }
      }
    }
  }
}
