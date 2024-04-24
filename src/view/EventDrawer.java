package view;

import model.Event;
import java.awt.Dimension;
import java.awt.Graphics;

public interface EventDrawer {
  /**
   * Draws the event on the calendar.
   */
  void drawMainSystem(Graphics g, Event event, Dimension size, boolean isHost);

  void drawSaturdayPlanner(Graphics g, Event event, int dayIndex, boolean isHost);
}
