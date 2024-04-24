import org.junit.Test;

import model.Event;
import model.SaturdayPlanner;
import model.Schedule;
import model.User;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SaturdayPlannerTest {

  @Test
  public void testGetEventsForWeekStarting() {
    SaturdayPlanner planner = new SaturdayPlanner();
    LocalDate saturday = LocalDate.of(2024, 4, 27); // Ensure this is a Saturday
    User user = new User("1", "Jane");
    planner.uploadSchedule("jane.xml", user);

    List<Event> events = planner.getEventsForWeekStarting(user, saturday);
     for (Event event : events) {
       System.out.println(event.getName());
     }
    assertEquals("Expected number of events does not match", 2, events.size());
  }
}
