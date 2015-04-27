package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * Used to create an action on a {@link GuiComponent}. Can be implemented as an
 * anonymous inner class or implemented in the top-level class. The GuiComponent
 * constructor automatically adds it if it is implemented.
 * <p>
 * <b>Example:</b>
 *
 * <pre>
 * // As anonymous inner class
 * GuiComponent component; // create instance
 * component.addActionListener(new ActionPerformed() {
 *     &#064;Override
 *     public void action(GuiEvent event) {
 *         // Do thing
 *     }
 * });
 *
 * // or as top-level class
 * public class MyComponent extends GuiComponent implements ActionPerformed {
 *
 *     &#064;Override
 *     public void action(GuiEvent event) {
 *         // do things
 *     }
 * }
 * </pre>
 *
 * @author Matthew
 */
public interface ActionPerformed {

    /**
     * Called when the component is clicked
     *
     * @param event The event containing component clicked
     */
    void action(GuiEvent event);

}
