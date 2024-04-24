package controller;


import javax.swing.JOptionPane;

import model.Event;
import model.PlannerSystem;
import model.User;
import view.MainSystemFrame;
import view.IPlannerViewListener;

/**
 * This class represents the PlannerController.
 * This class is used to control the planner system.
 * It listens for events from the view and updates the model accordingly.
 */
public class PlannerController implements IPlannerController {
  private final MainSystemFrame view;

  /**
   * Constructs a PlannerController with the given model and view.
   * This controller listens for events from the view and updates the model accordingly.
   *
   * @param model the planner system model
   * @param view  the main system frame view
   */
  public PlannerController(PlannerSystem model, MainSystemFrame view) {
    this.view = view;
    this.view.setListener(new IPlannerViewListener() {
      @Override
      public void onEventCreate(Event event, String userId) {
        model.addEventToUserSchedule(userId, event);
        view.updateSchedule(model.getEvents());
      }

      @Override
      public void onScheduleLoad(String filePath, User user) {
        try {
          model.uploadSchedule(filePath, user);
          view.updateSchedule(model.getEvents());
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "Error loading schedule: " + e.getMessage());
        }
      }
    });
  }

  @Override
  public void start() {
    view.setVisible(true);
  }
}
