import org.junit.Test;

import model.Event;
import model.SaturdayPlanner;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * This class tests the SaturdayPlanner class.
 * It tests the getEventsForWeekStarting method.
 */
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
    assertEquals("Expected number of events does not match", 3, events.size());
  }

  @Test
  public void testGetEventsForWeekStarting2() {
    SaturdayPlanner planner = new SaturdayPlanner();
    LocalDate saturday = LocalDate.of(2024, 4, 27);
    User user = new User("2", "John");
    planner.uploadSchedule("john.xml", user);

    List<Event> events = planner.getEventsForWeekStarting(user, saturday);
    for (Event event : events) {
      System.out.println(event.getName());
    }
    assertEquals("Expected number of events does not match", 2, events.size());
  }


  @Test
  public void testOtherMethods() {
    SaturdayPlanner planner = new SaturdayPlanner();
    LocalDate saturday = LocalDate.of(2024, 4, 27);
    User user = new User("2", "John");
    planner.uploadSchedule("john.xml", user);
    user.getSchedule().getEvents().forEach(event -> System.out.println(event.getName()));
    assertEquals("Expected number of events does not match",
            2, user.getSchedule().getEvents().size());

    // Set up in planner system, uploaded 2 xml files, 6 events in total
    planner.getEvents().forEach(event -> System.out.println(event.getName()));
    assertEquals("Expected number of events does not match",
            6, planner.getEvents().size());

    List<String> teamates = new ArrayList<>();
    teamates.add("John");
    teamates.add("Bob");
    teamates.add("Alice");
    LocalDateTime start = LocalDateTime.of(2024, 4,
            27, 10, 0);
    LocalDateTime end = LocalDateTime.of(2024, 4,
            27, 12, 0);
    Event newEvent = new Event("Volleyball", start,
            end, "Gym", false, teamates, "John");
    planner.createEvent(user, newEvent);
    assertEquals("Expected number of events does not match",
            3, user.getSchedule().getEvents().size());

    User u = planner.getUser("1"); // defined in planner system
    assertEquals("Expected user does not match",
            "John", u.getName());

    planner.removeEvent(user, newEvent);
    assertEquals("Expected number of events does not match",
            2, user.getSchedule().getEvents().size());

    planner.autoSchedule(user, newEvent);
    assertEquals("Expected number of events does not match",
            3, user.getSchedule().getEvents().size());

    Event soccer = new Event("soccer", start,
            end, "field", true, teamates, "Bob");
    planner.modifyEvent(user, soccer, newEvent);
    assertEquals("Expected number of events does not match",
            3, user.getSchedule().getEvents().size());
  }


}
