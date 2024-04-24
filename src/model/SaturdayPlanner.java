package model;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is another model class called SaturdayPlanner.
 * This class extends the already defined previous model class called PlannerSystem.
 * This class is used to get the events for the week starting on Saturday.
 * This model is intended to used in the Gui where Saturday is the first day of the week.
 */
public class SaturdayPlanner extends PlannerSystem {

  public SaturdayPlanner() {
    super();
  }

  @Override
  public List<Event> getEventsForWeekStarting(User user, LocalDate startDate) {
    LocalDate saturday = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
    LocalDate nextSaturday = saturday.plusDays(7);

    return user.getSchedule().getEvents().stream()
            .filter(event -> event.getStartTime().toLocalDate().
                    isAfter(saturday.minusDays(1))
                    && event.getStartTime().toLocalDate().isBefore(nextSaturday))
            .sorted(Comparator.comparing(Event::getStartTime))
            .collect(Collectors.toList());
  }

}
