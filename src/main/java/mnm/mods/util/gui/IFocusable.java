package mnm.mods.util.gui;

/**
 * Applied to a {@link GuiComponent} extending class to make it focusable.
 * Currently implemented in {@link GuiText} and {@link GuiSettingString}.
 *
 * @author Matthew
 */
public interface IFocusable {

    /**
     * Sets the component's focus
     *
     * @param focus The focus value
     */
    void setFocused(boolean focus);

    /**
     * Gets the current focus of this component.
     *
     * @return The focus
     */
    boolean isFocused();
}
