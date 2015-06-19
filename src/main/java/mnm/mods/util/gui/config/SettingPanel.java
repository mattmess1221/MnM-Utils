package mnm.mods.util.gui.config;

import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;

/**
 * Base class for a setting panel.
 */
public abstract class SettingPanel<T extends SettingsFile<?>> extends GuiPanel {

    private String displayString;

    /**
     * Called when this category is activated and displayed.
     */
    public void initGUI() {}

    /**
     * Sets the display string for this category.
     *
     * @param string The display string
     */
    public void setDisplayString(String string) {
        this.displayString = string;
    }

    /**
     * Gets the display string for this category. It is shown with the button
     * used to activate it.
     *
     * @return The display string
     */
    public String getDisplayString() {
        return this.displayString;
    }

    /**
     * Gets the {@link SettingsFile} used for this category. Used for loading and
     * saving the settings file.
     *
     * @return The settings
     */
    public abstract T getSettings();

    /**
     * Saves all the settings
     */
    public void saveSettings() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).saveValue();
            }
        }
    }

    /**
     * Resets all the settings to their value.
     */
    public void reset() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).reset();
            }
        }
    }

    /**
     * Resets all the settings to their defaults.
     */
    public void setDefault() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).setDefault();
            }
        }
    }

}
