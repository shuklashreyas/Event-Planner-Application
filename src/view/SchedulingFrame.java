package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


import model.AnytimeSchedulingStrategy;
import model.Event;
import model.PlannerSystem;
import model.User;

/**
 * This class represents a frame for scheduling events.
 * This is the new frame when a person clicks on the schedule event button.
 */
public class SchedulingFrame extends JFrame {
  private final PlannerSystem model;
  private final AnytimeSchedulingStrategy strat;
  private JTextField eventNameField;
  private JTextField locationField;
  private JCheckBox onlineCheckBox;
  private JComboBox<String> userComboBox;

  /**
   * Constructs a new SchedulingFrame.
   */
  public SchedulingFrame() {
    createUI();
    this.model = new PlannerSystem();
    this.strat = new AnytimeSchedulingStrategy();
  }

  /**
   * The main method to start the application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        SchedulingFrame sf = new SchedulingFrame();
        sf.setVisible(true);
      }
    });
  }

  private void createUI() {
    setTitle("Schedule Event");
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    eventNameField = new JTextField();
    locationField = new JTextField();
    onlineCheckBox = new JCheckBox("Not online");
    JTextField durationField = new JTextField();
    userComboBox = new JComboBox<>(new String[]{"Prof. Lucia", "Jane", "Student Anon"});
    JButton scheduleButton = new JButton("Schedule event");


    mainPanel.add(new JLabel("Event name:"));
    mainPanel.add(eventNameField);
    mainPanel.add(new JLabel("Location:"));
    mainPanel.add(locationField);
    mainPanel.add(onlineCheckBox);
    mainPanel.add(new JLabel("Duration in minutes:"));
    mainPanel.add(durationField);
    mainPanel.add(new JLabel("Available users"));
    mainPanel.add(userComboBox);

    scheduleButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String eventName = eventNameField.getText();
        String location = locationField.getText();
        boolean online = onlineCheckBox.isSelected();
        String selectedUserName = (String) userComboBox.getSelectedItem();
        User user = model.getUserByName(selectedUserName);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextOrSameThursday = now.with(TemporalAdjusters
                .nextOrSame(DayOfWeek.THURSDAY));
        LocalDateTime startSearch = nextOrSameThursday.withHour(0).withMinute(0);
        LocalDateTime endSearch = startSearch.plusDays(6);
        Event event = new Event(eventName, startSearch, endSearch, location,
                online, new ArrayList<>(), user.getName());

        // Anytime Scheduling strategy
        strat.scheduleEvent(event, user, model);
        JOptionPane.showMessageDialog(SchedulingFrame.this,
                "Event scheduled for " + event.getStart());
      }
    });

    add(mainPanel, BorderLayout.CENTER);
    add(scheduleButton, BorderLayout.SOUTH);
  }
}
