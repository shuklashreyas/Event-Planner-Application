package view;

import java.util.List;

import model.Event;

/**
 * This class represents a mock planner view for testing purposes.
 * It implements the IPlannerView interface.
 */
public class MockPlannerView implements IPlannerView {
  private List<Event> events;
  private boolean errorShown;

  @Override
  public void updateSchedule(List<Event> events) {
    System.out.println("Events:");
    for (Event event : events) {
      System.out.println(event);
    }
  }

  @Override
  public void setListener(IPlannerViewListener listener) {
    System.out.println("Listener set");
  }

  @Override
  public void showError(String message) {
    errorShown = true;
    System.out.println(message);
  }

  @Override
  public void setVisible(boolean visible) {
    System.out.println("Visible: " + visible);
  }


  public void addEvent(Event event) {
    events.add(event);
  }

  public List<Event> getEvents() {
    return events;
  }

  public boolean isErrorShown() {
    return errorShown;
  }
}
