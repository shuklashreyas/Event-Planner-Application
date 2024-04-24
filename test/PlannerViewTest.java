import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import model.Event;
import model.User;
import view.MockPlannerView;
import view.IPlannerViewListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the PlannerView class.
 * The tests are done using a mock planner view.
 */
public class PlannerViewTest {

  private MockPlannerView mockPlannerView;
  private IPlannerViewListener listener;

  @Before
  public void setUp() {
    mockPlannerView = new MockPlannerView();
    listener = new IPlannerViewListener() {
      @Override
      public void onEventCreate(Event event, String userId) {
        mockPlannerView.addEvent(event);
      }

      @Override
      public void onScheduleLoad(String filePath, User user) {
        mockPlannerView.showError("Error loading schedule");
      }
    };
  }

  @Test
  public void testOnEventCreate() {
    Event event = new Event("Event", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
            "Home", true, List.of("1"), "1");
    listener.onEventCreate(event, "1");
    assertEquals(1, mockPlannerView.getEvents().size());
    assertEquals(event, mockPlannerView.getEvents().get(0));
  }

  @Test
  public void testOnScheduleLoad() {
    User user = new User("1", "Alice");
    listener.onScheduleLoad("schedule.txt", user);
    assertTrue(mockPlannerView.isErrorShown());
  }

}
