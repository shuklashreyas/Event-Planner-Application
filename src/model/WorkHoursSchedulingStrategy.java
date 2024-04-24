package model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * This class represents the WorkHoursSchedulingStrategy.
 * This scheduling strategy will find the first possible time.
 * The strategy must find a block between Monday and Friday from 0900 to 1700.
 */
public class WorkHoursSchedulingStrategy implements ISchedulingStrategy {

  @Override
  public void scheduleEvent(Event event, User user, PlannerSystem plannerSystem) {
    Schedule userSchedule = user.getSchedule();
    LocalDateTime startSearch = LocalDateTime.now().with(DayOfWeek.MONDAY)
            .withHour(9).withMinute(0);
    LocalDateTime endSearch = startSearch.plusDays(4)
            .withHour(17).withMinute(0);

    SearchingStrategy.search(event, plannerSystem, userSchedule, startSearch, endSearch);
  }


}