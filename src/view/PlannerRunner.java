package view;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import controller.PlannerController;
import model.AnytimeSchedulingStrategy;
import model.Event;
import model.LenientSchedulingStrategy;
import model.PlannerSystem;
import model.ISchedulingStrategy;
import model.User;
import model.WorkHoursSchedulingStrategy;

/**
 * This class represents the GUI for the planner system.
 */
public class PlannerRunner {
  /**
   * The main method for the planner system.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    PlannerSystem model = new PlannerSystem();
    MainSystemFrame view = new MainSystemFrame(model);
    PlannerController controller = new PlannerController(model, view);

    // Check for command-line arguments and set the scheduling strategy accordingly
    if (args.length > 0) {
      ISchedulingStrategy strategy;
      switch (args[0].toLowerCase()) {
        case "anytime":
          strategy = new AnytimeSchedulingStrategy();
          break;
        case "workhours":
          strategy = new WorkHoursSchedulingStrategy();
          break;
        case "lenient":
          strategy = new LenientSchedulingStrategy();
          break;
        default:
          System.out.println("Unknown scheduling strategy specified. Defaulting to 'anytime'.");
          strategy = new AnytimeSchedulingStrategy();
      }
      model.setSchedulingStrategy(strategy);
    } else {
      System.out.println("No scheduling strategy specified. Defaulting to 'anytime'.");
      ISchedulingStrategy strategy = new AnytimeSchedulingStrategy();
      model.setSchedulingStrategy(strategy);
    }

    controller.start();

  }

}

