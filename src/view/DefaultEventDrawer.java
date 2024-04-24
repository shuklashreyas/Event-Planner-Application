package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.Event;

/**
 * This class represents the DefaultEventDrawer.
 * This class is used to draw the event on the calendar.
 * It handles the drawing of the event rectangle.
 * As well as the color of the event rectangle.
 * The color of the event rectangle is red for non-host events and cyan for host events.
 */
public class DefaultEventDrawer implements EventDrawer {
  @Override
  public void drawMainSystem(Graphics g, Event event, Dimension size, boolean isHost) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Calculate the position and size of the event rectangle
    int startX = event.getStartTime().getDayOfWeek().getValue() % 7 * size.width;
    int startY = event.getStartTime().getHour() * size.height / 24
            + event.getStartTime().getMinute() * size.height / 1440;
    int endY = event.getEndTime().getHour() * size.height / 24
            + event.getEndTime().getMinute() * size.height / 1440;
    int height = endY - startY;

    // Set a default color for non-host events
    g2d.setColor(Color.RED);

    if (isHost) {
      g2d.setColor(Color.CYAN);
    }

    // Draw the event
    g2d.fillRect(startX, startY, size.width, height);
    g2d.dispose();
  }


}
