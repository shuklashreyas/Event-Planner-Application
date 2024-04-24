package view;

import java.awt.*;
import java.time.LocalDateTime;

import model.Event;

public class DefaultEventDrawer implements EventDrawer{
  @Override
  public void drawMainSystem(Graphics g, Event event, Dimension size, boolean isHost) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Calculate the position and size of the event rectangle
    int startX = event.getStartTime().getDayOfWeek().getValue() % 7 * size.width;
    int startY = event.getStartTime().getHour() * size.height / 24 +
            event.getStartTime().getMinute() * size.height / 1440;
    int endY = event.getEndTime().getHour() * size.height / 24 +
            event.getEndTime().getMinute() * size.height / 1440;
    int height = endY - startY;

    // Set a default color for non-host events
    g2d.setColor(Color.RED);

   if(isHost) {
     g2d.setColor(Color.CYAN);
   }

    // Draw the event
    g2d.fillRect(startX, startY, size.width, height);

    // Clean up
    g2d.dispose();
  }

  @Override
  public void drawSaturdayPlanner(Graphics g, Event event, int dayIndex, boolean isHost) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Calculate the position and size of the event rectangle
    int startX = dayIndex * 100;
    int startY = event.getStartTime().getHour() * 20 +
            event.getStartTime().getMinute() * 20 / 60;
    int endY = event.getEndTime().getHour() * 20 +
            event.getEndTime().getMinute() * 20 / 60;
    int height = endY - startY;

    // Set a default color for non-host events
    g2d.setColor(Color.RED);

    if(isHost) {
      g2d.setColor(Color.CYAN);
    }

    // Draw the event
    g2d.fillRect(startX, startY, 100, height);

    // Clean up
    g2d.dispose();
  }



}
