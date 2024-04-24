package model;

import java.time.LocalDateTime;

/**
 * This class represents the SearchingStrategy.
 * This class is used to search for the first possible time for an event to be scheduled.
 */
public class SearchingStrategy {
  static void search(Event event, PlannerSystem plannerSystem, Schedule userSchedule,
                     LocalDateTime startSearch, LocalDateTime endSearch) {
    while (startSearch.isBefore(endSearch)) {
      LocalDateTime endSearchTime = startSearch.plusMinutes(event.getDuration());
      if (userSchedule.isAvailable(startSearch, endSearchTime)) {
        boolean allInviteesAvailable = true;
        for (String inviteeId : event.getInvitees()) {
          User invitee = plannerSystem.getUser(inviteeId);
          if (invitee == null || !invitee.getSchedule().isAvailable(startSearch, endSearchTime)) {
            allInviteesAvailable = false;
            break;
          }
        }
        if (allInviteesAvailable) {
          event.setTime(startSearch);
          userSchedule.addEvent(event);
          for (String inviteeId : event.getInvitees()) {
            User invitee = plannerSystem.getUser(inviteeId);
            invitee.getSchedule().addEvent(event);
          }
          return;
        }
      }
      startSearch = startSearch.plusMinutes(30);
      if (!startSearch.isBefore(endSearch)) {
        break;
      }
    }
  }
}
