package view;

import model.Event;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This interface represents the EventDrawer.
 * This interface is used to draw the event on the calendar.
 */
public interface EventDrawer {
  /**
   * Draws the event on the calendar.
   */
  void drawMainSystem(Graphics g, Event event, Dimension size, boolean isHost);

}
