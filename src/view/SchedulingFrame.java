package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
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


import model.Event;
import model.IReadOnlyModel;
import model.PlannerSystem;
import model.User;
import model.WorkHoursSchedulingStrategy;

/**
 * This class represents a frame for scheduling events.
 * This is the new frame when a person clicks on the schedule event button.
 */
public class SchedulingFrame extends JFrame {
  private final WorkHoursSchedulingStrategy strat;
  private JTextField eventNameField;
  private JTextField locationField;
  private JCheckBox onlineCheckBox;
  private JComboBox<String> userComboBox;



  /**
   * Constructs a new SchedulingFrame.
   */
  public SchedulingFrame(IReadOnlyModel model) {

    this.strat = new WorkHoursSchedulingStrategy();



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
    userComboBox = new JComboBox<>();
    for (User user : model.getUsers()) {
      userComboBox.addItem(user.getName());
    }
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


        Duration duration = Duration.ofMinutes(Long.parseLong(durationField.getText()));
        Event event = new Event(eventName, duration, location, online, new ArrayList<>());


        // Anytime Scheduling strategy
        strat.scheduleEvent(event, user, model);
        JOptionPane.showMessageDialog(SchedulingFrame.this,
                "Event scheduled for " + event.getStart());

        for (Event s : user.getEvents()) {
          System.out.println(s);
        }

        //MainSystemFrame.refreshScheduleDisplay();
      }
    });

    add(mainPanel, BorderLayout.CENTER);
    add(scheduleButton, BorderLayout.SOUTH);
  }


  /**
   * The main method to start the application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        IReadOnlyModel model = new PlannerSystem();
        new SchedulingFrame(model).setVisible(true);
      }
    });
  }

}
