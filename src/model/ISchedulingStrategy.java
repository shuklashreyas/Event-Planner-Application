package model;

/**
 * This interface represents the SchedulingStrategy.
 * This interface is used to schedule an event.
 * Different scheduling strategies will implement this interface.
 * Based on the criteria of the scheduling strategy, the event will be scheduled.
 */
public interface ISchedulingStrategy {
  void scheduleEvent(Event event, User user,  PlannerSystem plannerSystem);
}

