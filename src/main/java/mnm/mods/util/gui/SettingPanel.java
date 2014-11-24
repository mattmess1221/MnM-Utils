package mnm.mods.util.gui;

import mnm.mods.util.Settings;

/**
 * Base class for a setting panel.
 */
public abstract class SettingPanel extends GuiPanel {

    /**
     * Called when this category is activated and displayed.
     */
    public void initGUI() {
    }

    /**
     * Gets the display string for this category. It is shown with the button
     * used to activate it.
     *
     * @return The display string
     */
    public abstract String getDisplayString();

    /**
     * Gets the {@link Settings} used for this category. Used for loading and
     * saving the settings file.
     *
     * @return The settings
     */
    public abstract Settings getSettings();

    public void saveSettings() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).saveValue();
            }
        }
    }

    public void reset() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).reset();
            }
        }
    }

    public void setDefault() {
        for (GuiComponent comp : this) {
            if (comp instanceof GuiSetting) {
                ((GuiSetting<?>) comp).setDefault();
            }
        }
    }

}
