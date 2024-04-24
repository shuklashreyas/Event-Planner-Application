package model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * This class represents the AnytimeSchedulingStrategy.
 * This scheduling strategy will find the first possible time.
 * Allows all invitees and the host to be present
 */
public class AnytimeSchedulingStrategy implements ISchedulingStrategy {

  @Override
  public void scheduleEvent(Event event, User host, PlannerSystem plannerSystem) {
    Schedule hostSchedule = host.getSchedule();
    LocalDateTime startSearch = LocalDateTime.now().with(DayOfWeek.SUNDAY)
            .withHour(0).withMinute(0);
    LocalDateTime endSearch = startSearch.plusDays(6)
            .withHour(23).withMinute(59);
    SearchingStrategy.search(event, plannerSystem, hostSchedule, startSearch, endSearch);


  }
}
