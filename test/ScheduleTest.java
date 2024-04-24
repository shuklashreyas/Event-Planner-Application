
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import model.Event;
import model.Schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for testing functionality of the Schedule class.
 */
public class ScheduleTest {
  private Schedule schedule;
  private Event event1;
  private Event event2;

  @Before
  public void setUp() {
    schedule = new Schedule();
    LocalDateTime startTime = LocalDateTime.of(2024, 3,
            10, 10, 0);
    LocalDateTime endTime = LocalDateTime.of(2024, 3,
            10, 11, 0);
    event1 = new Event("Test Event 1", startTime, endTime,
            "Location 1", false, null, "user1");
    event2 = new Event("Test Event 2", startTime.plusHours(2),
            endTime.plusHours(3), "Location 2",
            true, null, "user1");
  }

  @Test
  public void addEvent() {
    // Testing that addevent actually adds the event
    schedule.addEvent(event1);
    assertTrue("The schedule should contain the added event",
            schedule.getEvents().contains(event1));
  }

  @Test
  public void removeEvent() {
    // Testing that removeEvent actually removes the event
    schedule.addEvent(event1);
    schedule.addEvent(event2);
    schedule.removeEvent(event1);
    assertFalse("The schedule should not contain the removed event",
            schedule.getEvents().contains(event1));
    assertTrue("The schedule should still contain other events not removed",
            schedule.getEvents().contains(event2));
  }

  @Test
  public void getEvents() {
    // adding events to then use the getter to get the events
    schedule.addEvent(event1);
    schedule.addEvent(event2);
    List<Event> events = schedule.getEvents();
    assertNotNull("getEvents should return a non-null list of events", events);
    assertTrue("The list of events should contain event1", events.contains(event1));
    assertTrue("The list of events should contain event2", events.contains(event2));
    assertEquals("The list of events should contain exactly 2 events", 2,
            events.size());
  }

}