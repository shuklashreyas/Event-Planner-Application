package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class represents the planner system.
 * This class has a variety of different methods that allow for the manipulation of the schedule.
 * This class also has a map of users.
 * This is the main model class that implements the plannerSystemModel interface.
 * Class Invariants:
 * Every user has a unique ID.
 * For all events in a user's schedule, the start time is before the end time.
 * No two events in a user's schedule overlap in time.
 */
public class PlannerSystem implements IPlannerSystemModel {

  private Map<String, User> users = new HashMap<>();
  private ISchedulingStrategy schedulingStrategy;

  /**
   * Constructor for the PlannerSystem class.
   * This constructor creates a new planner system with two users and some events.
   * The users and events are hardcoded into the system.
   */
  public PlannerSystem() {

    User user1 = new User("1", "John");
    User user2 = new User("2", "Jane");

    uploadSchedule("john.xml", user1);
    uploadSchedule("jane.xml", user2);

    this.addUser(user1);
    this.addUser(user2);
  }
  @Override
  public void setSchedulingStrategy(ISchedulingStrategy schedulingStrategy) {
    this.schedulingStrategy = schedulingStrategy;
  }

  @Override
  public List<Event> getEventsForWeekStarting(User user, LocalDate startDate) {
    List<Event> weekEvents = new ArrayList<>();
    LocalDate endDate = startDate.plusDays(7);
    for (Event event : user.getSchedule().getEvents()) {
      if (!event.getStartTime().toLocalDate().isBefore(startDate) &&
              event.getStartTime().toLocalDate().isBefore(endDate)) {
        weekEvents.add(event);
      }
    }
    return weekEvents;
  }

  @Override
  public boolean uploadSchedule(String xmlFilePath, User user) {
    if (user == null) {
      throw new IllegalArgumentException("User is null.");
    }
    if (xmlFilePath == null || xmlFilePath.isEmpty()) {
      throw new IllegalArgumentException("Invalid XML file path.");
    }

    try {
      File xmlFile = new File(xmlFilePath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("event");

      for (int i = 0; i < nList.getLength(); i++) {
        Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          String name = eElement.getElementsByTagName("name").item(0).getTextContent().replace("\"", "");
          String startDay = eElement.getElementsByTagName("start-day").item(0).getTextContent();
          String startTimeString = eElement.getElementsByTagName("start").item(0).getTextContent();
          String endDay = eElement.getElementsByTagName("end-day").item(0).getTextContent();
          String endTimeString = eElement.getElementsByTagName("end").item(0).getTextContent();
          boolean isOnline = Boolean.parseBoolean(eElement.getElementsByTagName("online").item(0).getTextContent());
          String place = eElement.getElementsByTagName("place").item(0).getTextContent().replace("\"", "");
          ArrayList<String> invitees = new ArrayList<>();
          String hostId = null;

          NodeList userNodes = eElement.getElementsByTagName("users").item(0).getChildNodes();
          for (int j = 0; j < userNodes.getLength(); j++) {
            Node uidNode = userNodes.item(j);
            if (uidNode.getNodeType() == Node.ELEMENT_NODE) {
              String uid = removeQuotes(uidNode.getTextContent().trim());
              invitees.add(uid);
              if (hostId == null) {
                hostId = uid;
              }
            }
          }

          DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
          LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
          LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);
          LocalDate startDate = LocalDate.now().with(TemporalAdjusters
                  .next(DayOfWeek.valueOf(startDay.toUpperCase())));
          LocalDate endDate = LocalDate.now().with(TemporalAdjusters
                  .next(DayOfWeek.valueOf(endDay.toUpperCase())));
          LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
          LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);


          Event event = new Event(name, startDateTime, endDateTime, place, isOnline, invitees, hostId);
          user.getSchedule().addEvent(event);
        }
      }
      return true;
    } catch (IOException e) {
      throw new IllegalStateException("Error reading the XML file", e);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  private String removeQuotes(String text) {
    if (text == null) return null;
    return text.replace("\"", "");  // Remove all double quotes
  }

  @Override
  public boolean saveSchedule(String xmlFilePath, User user) {
    if (user == null) {
      throw new IllegalArgumentException("User is null.");
    }

    if (xmlFilePath == null || xmlFilePath.isEmpty()) {
      throw new IllegalArgumentException("Invalid XML file path.");
    }

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      // Root element
      Element rootElement = doc.createElement("schedule");
      doc.appendChild(rootElement);
      rootElement.setAttribute("id", user.getId());

      List<Event> events = user.getSchedule().getEvents();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

      for (Event event : events) {
        Element eventElement = doc.createElement("event");
        rootElement.appendChild(eventElement);

        // Populate the eventElement with details from the Event object
        // Similar to the example provided previously

        // Write the content into XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(xmlFilePath));

        transformer.transform(source, result);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void selectUser(User user) {
    if (user == null || !users.containsKey(user.getId())) {
      throw new IllegalArgumentException("User not found in the system.");
    }
  }

  @Override
  public void addUser(User user) {
    if (user == null || users.containsKey(user.getId())) {
      throw new IllegalArgumentException("Invalid user or user already exists.");
    }
    users.put(user.getId(), user);
  }

  @Override
  public User getUser(String userId) {
    return users.get(userId);
  }

  @Override
  public void selectAndModifyUserSchedule(String userId) {
    User user = getUser(userId);
    if (user != null) {
      selectUser(user);
    } else {
      System.out.println("User not found");
    }
  }

  @Override
  public boolean createEvent(User user, Event event) {
    // Validate input parameters
    if (user == null || event == null) {
      throw new IllegalArgumentException("User or event cannot be null.");
    }

    // Ensure the user exists in the system
    if (!users.containsKey(user.getId())) {
      throw new IllegalArgumentException("User does not exist in the system.");
    }

    // Check for event conflicts before adding the event
    if (user.getSchedule().hasEventConflict(event)) {
      throw new IllegalStateException("The event conflicts with an existing event.");
    }

    // Attempt to add the event to the user's schedule
    try {
      user.getSchedule().addEvent(event);
      return true;
    } catch (Exception e) {
      throw new IllegalStateException("Error creating the event", e);
    }
  }


  @Override
  public boolean modifyEvent(User user, Event originalEvent, Event updatedEvent) {

    if (user == null || originalEvent == null || updatedEvent == null) {
      throw new IllegalArgumentException("User, original event, or updated event is null.");
    }

    if (originalEvent.equals(updatedEvent)) {
      throw new IllegalArgumentException("Original event and updated event are the same.");
    }

    if (!users.containsKey(user.getId())) {
      throw new IllegalStateException("User does not exist in the system.");
    }
    try {
      user.getSchedule().removeEvent(originalEvent);
      user.getSchedule().addEvent(updatedEvent);
      return true;
    } catch (Exception e) {
      throw new IllegalStateException("Error modifying the event", e);
    }
  }

  @Override
  public boolean removeEvent(User user, Event event) {
    if (user == null || event == null) {
      throw new IllegalArgumentException("User or event is null.");
    }

    if (!users.containsKey(user.getId())) {
      throw new IllegalStateException("User does not exist in the system.");
    }
    try {
      user.getSchedule().removeEvent(event);
      return true;
    } catch (Exception e) {
      throw new IllegalStateException("Error removing the event", e);
    }
  }

  @Override
  public boolean autoSchedule(User user, Event event) {
    // First availabe open time slot, cant conflict with other events
    if (user == null || event == null) {
      throw new IllegalArgumentException("User or event is null.");
    }
    if (!users.containsKey(user.getId())) {
      throw new IllegalStateException("User does not exist in the system.");
    }
    try {
      user.getSchedule().addEvent(event);
      return true;
    } catch (Exception e) {
      throw new IllegalStateException("Error scheduling the event", e);
    }
  }

  @Override
  public List<Event> seeEvents(User user, LocalDateTime time) {
    if (user == null || time == null) {
      throw new IllegalArgumentException("User or time is null.");
    }

    if (!users.containsKey(user.getId())) {
      throw new IllegalStateException("User does not exist in the system.");
    }
    List<Event> events = user.getSchedule().getEvents();
    List<Event> eventsAtTime = new ArrayList<>();
    for (Event event : events) {
      if (event.getStartTime().isBefore(time) && event.getEndTime().isAfter(time)) {
        eventsAtTime.add(event);
      }
    }
    return eventsAtTime;
  }

  @Override
  public List<User> getUsers() {
    return new ArrayList<>(users.values());
  }

  @Override
  public List<Event> getEvents() {
    List<Event> events = new ArrayList<>();
    for (User user : users.values()) {
      events.addAll(user.getSchedule().getEvents());
    }
    return events;
  }

  @Override
  public User getUserByName(String name) {
    for (User user : users.values()) {
      if (user.getName().equals(name)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public void addEventToUserSchedule(String selectedUser, Event event) {
    User user = getUser(selectedUser);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    user.getSchedule().addEvent(event);
  }

  @Override
  public void createEventBasedOnStrategy(User user, Event event) {
    if (schedulingStrategy == null) {
      throw new IllegalStateException("Scheduling strategy is not set.");
    }
    schedulingStrategy.scheduleEvent(event, user, this);
  }



}