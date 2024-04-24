package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.swing.*;

import model.Event;
import model.PlannerSystem;
import model.SaturdayPlanner;
import model.User;

public class SaturdayViewFrame extends JFrame implements IPlannerView, IPlannerViewListener {
  private static JPanel schedulePanel;
  private JButton createEventButton;
  private JButton scheduleEventButton;

  private JButton toggleHostButton;
  private JComboBox<String> userComboBox;
  private SaturdayPlanner readOnlyModel; // Use SaturdayPlanner specifically for the view

  private List<Event> currentEvents;
  private IPlannerViewListener viewListener;

  private EventDrawer eventDrawer;

  private boolean hostColorModeEnabled = false;

  /**
   * Constructs a MainSystemFrame with the given model.
   *
   * @param model the model to use
   */
  public SaturdayViewFrame(SaturdayPlanner model) {
    this.readOnlyModel = model;
    this.eventDrawer = new DefaultEventDrawer();
    initializeMenu();
    initializeSchedulePanel();
    initializeButtons();
    initializeUserComboBox();
    setupFrameLayout();
    configureFrame();
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        schedulePanel.repaint();
      }
    });

  }

  /**
   * The main method to start the application.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new SaturdayViewFrame(new SaturdayPlanner());
      }
    });


  }

  /**
   * Refreshes the schedule display.
   */
  public static void refreshScheduleDisplay() {
    schedulePanel.repaint();
  }

  private void initializeUserComboBox() {
    userComboBox = new JComboBox<>();
    List<User> users = readOnlyModel.getUsers();
    for (User user : users) {
      userComboBox.addItem(user.getName());
    }

    userComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selectedUser = (String) userComboBox.getSelectedItem();
        if (selectedUser != null) {
          User user = readOnlyModel.getUserByName(selectedUser);
          if (user != null) {
            currentEvents = readOnlyModel.getEventsForWeekStarting(user,
                    LocalDate.of(2024, 4, 27));
            if (currentEvents != null) {
              schedulePanel.repaint();
            } else {
              System.out.println("The event list for the user is null.");
            }
          } else {
            System.out.println("No user found with the name: " + selectedUser);
          }
        } else {
          System.out.println("No user is selected.");
        }
      }
    });
  }


  private void initializeMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");

    JMenuItem addCalendarMenuItem = new JMenuItem("Load calendar");
    addCalendarMenuItem.addActionListener(e -> openFileChooserForLoad());

    JMenuItem saveCalendarsMenuItem = new JMenuItem("Save calendars");
    saveCalendarsMenuItem.addActionListener(e -> openFileChooserForSave());

    fileMenu.add(addCalendarMenuItem);
    fileMenu.add(saveCalendarsMenuItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);
  }
  private void drawEvent(Graphics g, Event event, int dayIndex) {
    Graphics2D g2d = (Graphics2D) g;
    Dimension size = new Dimension(getWidth() / 7, getHeight());
    int startX = dayIndex * size.width;
    int startY = calculateYPosition(event.getStartTime());
    int endY = calculateYPosition(event.getEndTime());
    int height = endY - startY;
    g2d.setColor(Color.RED);
    g2d.fillRect(startX, startY, size.width, height);
  }

  private int calculateYPosition(LocalDateTime startTime) {
    int hourHeight = getHeight() / 24;
    return startTime.getHour() * hourHeight + startTime.getMinute() * hourHeight / 60;
  }

  private void initializeSchedulePanel() {
    schedulePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawScheduleGrid(g);
        if (currentEvents != null && !currentEvents.isEmpty()) {
          for (Event event : currentEvents) {
            DayOfWeek dayOfWeek = event.getStartTime().getDayOfWeek();
            int dayIndex = (dayOfWeek.getValue() + 1) % 7;
            drawEvent(g, event, dayIndex);
          }
        }
      }

    };

    schedulePanel.setPreferredSize(new Dimension(800, 600));

    schedulePanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        // Calculate which tile was clicked
        int dayWidth = schedulePanel.getWidth() / 7;
        int hourHeight = schedulePanel.getHeight() / 24;
        int dayIndex = e.getX() / dayWidth;
        int hourIndex = e.getY() / hourHeight;

        DayOfWeek day = DayOfWeek.of((dayIndex + 1) % 7 + 1);
        LocalTime time = LocalTime.of(hourIndex, 0);
        LocalDate date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day));
        LocalDateTime startDateTime = LocalDateTime.of(date, time);
        EventFrame eventFrame = new EventFrame(readOnlyModel);
        eventFrame.setStartTime(startDateTime);
        eventFrame.setVisible(true);
      }
    });


    schedulePanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int clickedX = e.getX();
        int clickedY = e.getY();

        int dayWidth = getWidth() / 7;
        int hourHeight = getHeight() / 24;

        int clickedDayIndex = clickedX / dayWidth + 1;
        LocalTime timeClicked = LocalTime.of(clickedY / hourHeight, 0);

        for (Event event : currentEvents) {
          if (eventOccursDuringClick(event, clickedDayIndex, timeClicked)) {
            EventFrame eventFrame = new EventFrame(readOnlyModel);
            eventFrame.populateEventDetails(event);
            eventFrame.setVisible(true);
            break;
          }
        }
      }

      private boolean eventOccursDuringClick(Event event, int clickedDayIndex,
                                             LocalTime timeClicked) {
        DayOfWeek dayOfWeekEventStart = event.getStartTime().getDayOfWeek();
        DayOfWeek dayOfWeekEventEnd = event.getEndTime().getDayOfWeek();
        DayOfWeek dayClicked = DayOfWeek.of(clickedDayIndex);

        boolean isSameDay = (dayOfWeekEventStart.equals(dayClicked) || dayOfWeekEventEnd.
                equals(dayClicked));
        boolean isSameTime = (timeClicked.equals(event.getStartTime().toLocalTime())
                || timeClicked.isAfter(event.getStartTime().toLocalTime()))
                && (timeClicked.isBefore(event.getEndTime().toLocalTime()));

        return isSameDay && isSameTime;
      }
    });


  }

  private void initializeButtons() {
    createEventButton = new JButton("Create event");
    scheduleEventButton = new JButton("Schedule event");
    toggleHostButton = new JButton("Toggle Host");

    createEventButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        EventFrame eventFrame = new EventFrame(readOnlyModel);
        eventFrame.setVisible(true);
      }
    });
    scheduleEventButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // opens the SchedulingFrame
        SchedulingFrame schedulingFrame = new SchedulingFrame();
        schedulingFrame.setVisible(true);

      }
    });

    toggleHostButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        hostColorModeEnabled = !hostColorModeEnabled;
        schedulePanel.repaint();  // Repaint the panel to reflect changes
        System.out.println("Host color mode " + (hostColorModeEnabled ? "enabled" : "disabled"));
      }
    });

  }

  private void setupFrameLayout() {
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(new JLabel("Select User:"));
    topPanel.add(userComboBox);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(createEventButton);
    buttonPanel.add(scheduleEventButton);
    buttonPanel.add(toggleHostButton);

    setLayout(new BorderLayout());
    add(topPanel, BorderLayout.NORTH);
    add(schedulePanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void configureFrame() {
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void drawScheduleGrid(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int panelHeight = schedulePanel.getHeight();
    int panelWidth = schedulePanel.getWidth();

    int hourHeight = panelHeight / 24;
    int dayWidth = panelWidth / 7;


    g2d.setColor(Color.LIGHT_GRAY);
    Stroke defaultStroke = g2d.getStroke();

    // Draw hour lines
    for (int i = 0; i <= 24; i++) {
      if (i % 4 == 0) { // Bold line for every fourth hour
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
      } else {
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.LIGHT_GRAY);
      }
      g2d.drawLine(0, i * hourHeight, panelWidth, i * hourHeight);
      g2d.setStroke(defaultStroke);
      g2d.setColor(Color.LIGHT_GRAY);
    }

    for (int i = 0; i <= 7; i++) {
      g2d.drawLine(i * dayWidth, 0, i * dayWidth, panelHeight);
    }
  }

  private void openFileChooserForLoad() {
    JFileChooser fileChooser = new JFileChooser();
    int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Load XML file: " + selectedFile.getAbsolutePath());

      PlannerSystem plannerSystem = new PlannerSystem();
      User currentUser = new User("1", "Host");
      boolean isUploaded = plannerSystem.uploadSchedule(selectedFile.getAbsolutePath(),
              currentUser);

      for (Event event : currentUser.getSchedule().getEvents()) {
        System.out.println(event.getName());
      }
      if (isUploaded) {
        // Schedule uploaded successfully, now update the view
        currentEvents = currentUser.getSchedule().getEvents(); // Get the updated events
        schedulePanel.repaint();
      } else {

        System.out.println("Failed to upload schedule from XML.");
      }
    }
  }

  private void openFileChooserForSave() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int option = fileChooser.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      File selectedDirectory = fileChooser.getSelectedFile();
      System.out.println("Save to directory: " + selectedDirectory.getAbsolutePath());
    }
  }


  @Override
  public void updateSchedule(List<Event> events) {
    currentEvents = events;
    schedulePanel.repaint();
  }

  @Override
  public void setListener(IPlannerViewListener listener) {
    this.viewListener = listener;
  }

  @Override
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void onEventCreate(Event event, String userId) {
    viewListener.onEventCreate(event, userId);
  }

  @Override
  public void onScheduleLoad(String filePath, User user) {
    viewListener.onScheduleLoad(filePath, user);
  }

}
