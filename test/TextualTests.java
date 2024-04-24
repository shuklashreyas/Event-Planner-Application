import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.PlannerSystem;
import model.User;
import view.TextualView;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the textual view of the planner system.
 * Checks if the textual view displays the users' schedules correctly.
 */
public class TextualTests {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private PlannerSystem ps;
  private TextualView tv;

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    ps = new PlannerSystem();
    tv = new TextualView();
    User user1 = new User("1", "Prof. Lucia");
    ps.addUser(user1);
    ps.uploadSchedule("prof.xml", user1);

    User user2 = new User("2", "Student Anon");
    ps.addUser(user2);
    ps.uploadSchedule("prof.xml", user2);
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void testDisplayUsersSchedules() {
    List<User> users = new ArrayList<>(ps.getUsers());
    tv.displayUsersSchedules(users);
    String output = outContent.toString();
    System.setOut(originalOut);
    System.out.println(output);
    assertTrue(output.contains("Prof. Lucia"));
    assertTrue(output.contains("Student Anon"));
  }

  @Test
  public void addAndRemoveEvent() {
    User user = ps.getUser("1");
    ps.selectAndModifyUserSchedule(user.getId());
    LocalDateTime startTime = LocalDateTime.parse("2021-10-01T10:00");
    LocalDateTime endTime = LocalDateTime.parse("2021-10-01T11:00");
    Event e = new Event("Football", startTime, endTime, "Online", true,
            new ArrayList<>(), user.getId());
    ps.createEvent(user, e);

    List<User> users = new ArrayList<>(ps.getUsers());
    tv.displayUsersSchedules(users);
    String output = outContent.toString();

    System.setOut(originalOut);
    System.out.println(output);

    assertTrue(output.contains("Football"));

    ps.removeEvent(user, e);
    outContent.reset();
    tv.displayUsersSchedules(users);
    output = outContent.toString();
    System.setOut(originalOut);
    System.out.println(output);
    assertFalse(output.contains("Football"));
  }

  @Test
  public void comprehensiveTest() {
    User user1 = new User("Shreyas", "1");
    ps.addUser(user1);
    ps.uploadSchedule("testfile.xml", user1);

    User user2 = new User("Brandon", "2");
    ps.addUser(user2);
    ps.uploadSchedule("testfile.xml", user2);

    // See Events for a user at a specific time
    LocalDateTime timeToCheck = LocalDateTime.of(2024, 3, 19, 10, 0); // Example time
    List<Event> eventsAtTime = ps.seeEvents(user1, timeToCheck);
    Assert.assertFalse(eventsAtTime.isEmpty());

    // Modify an event for Shreyas
    Event originalEvent = eventsAtTime.get(0);
    Event updatedEvent = new Event(originalEvent.getName(), originalEvent.getStartTime(),
            originalEvent.getEndTime().plusHours(1), originalEvent.getLocation(),
            originalEvent.isOnline(), originalEvent.getInvitees(),
            originalEvent.getHostId());
    ps.modifyEvent(user1, originalEvent, updatedEvent);

    // Auto schedule an event for Brandon
    Event autoScheduledEvent = new Event("Jogging", null, null,
            "Park", false, new ArrayList<>(), user2.getId());
    ps.autoSchedule(user2, autoScheduledEvent);
    ps.removeEvent(user1, updatedEvent);

    // Display updated schedules
    ArrayList<User> users = new ArrayList<>(List.of(user1, user2));
    tv.displayUsersSchedules(users);
  }

}
