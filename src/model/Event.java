package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The class for an event.
 * An event has a name, start time, end time, location, and participants, and invitees.
 * The start and end times are in LocalDateTime format.
 */
public class Event implements IEvent {
  private String name;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String location;
  private boolean isOnline;

  private List<String> invitees;

  private String hostId;

  private Duration Duration;



  /**
   * Constructor for the Event class.
   *
   * @param name      the name of the event
   * @param startTime the start time of the event
   * @param endTime   the end time of the event
   * @param location  the location of the event
   * @param isOnline  whether the event is online
   * @param invitees  the invitees of the event
   */

  public Event(String name, LocalDateTime startTime, LocalDateTime endTime,
               String location, boolean isOnline, List<String> invitees, String hostId) {
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.location = location;
    this.isOnline = isOnline;
    this.invitees = invitees;
    this.hostId = hostId;
  }

  public Event(String name, Duration duration,
               String location, boolean isOnline, List<String> invitees) {
    this.name = name;
    this.Duration = duration;
    this.location = location;
    this.isOnline = isOnline;
    this.invitees = invitees;
  }

  @Override
  public boolean checkForConflict(Event otherEvent) {
    return (this.startTime.isBefore(otherEvent.getEndTime())
            && this.endTime.isAfter(otherEvent.getStartTime()));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public LocalDateTime getStartTime() {
    return startTime;
  }

  @Override
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  @Override
  public LocalDateTime getEndTime() {
    return endTime;
  }

  @Override
  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public boolean isOnline() {
    return isOnline;
  }

  @Override
  public void setOnline(boolean online) {
    isOnline = online;
  }

  @Override
  public List<String> getInvitees() {
    return invitees;
  }

  @Override
  public void setInvitees(List<String> invitees) {
    this.invitees = invitees;
  }

  @Override
  public boolean conflictsWith(Event event) {
    return this.startTime.isBefore(event.getEndTime())
            && this.endTime.isAfter(event.getStartTime());
  }

  @Override
  public LocalDateTime getStart() {
    return startTime;
  }

  @Override
  public LocalDateTime getEnd() {
    return endTime;
  }

  @Override
  public long getDuration() {
    return Duration.toMinutes();
  }

  @Override
  public void setTime(LocalDateTime startSearch) {
    this.startTime = startSearch;
    this.endTime = startSearch.plusMinutes(getDuration());
  }

  @Override
  public String getHostId() {
    return hostId;
  }

  @Override
  public String toString() {
    return
            " name = '" + name + '\'' +
            ", startTime = " + startTime +
            ", endTime = " + endTime +
            ", location = " + location + '\'' +
            ", isOnline = " + isOnline +
            ", invitees = " + invitees +
            ", hostId = " + hostId ;

  }

  public void setDuration(Duration duration) {
    Duration = duration;
  }
}