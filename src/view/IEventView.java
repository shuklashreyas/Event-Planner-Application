package view;

import java.time.LocalDateTime;

import javax.swing.JComponent;
import javax.swing.JPanel;

import model.Event;

/**
 * This interface represents the view for the event panel.
 */
public interface IEventView {
  /**
   * Populate the event details in the frame.
   * This method is called when the user selects an event from the list.
   *
   * @param event the event to populate the details for
   */
  void populateEventDetails(Event event);

  /**
   * Set the start time of the event.
   *
   * @param startTime the start time of the event
   */
  void setStartTime(LocalDateTime startTime);

  /**
   * Create a label and component.
   *
   * @param labelText the label text
   * @param component the component
   * @return the panel
   */
  JPanel createLabelAndComponent(String labelText, JComponent component);

  /**
   * Initialize the user combo box choosing the user from the list.
   */
  void initializeUserComboBox();

}
