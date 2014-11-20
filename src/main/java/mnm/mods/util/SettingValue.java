package mnm.mods.util;

public class SettingValue<T> {

    private final T defaultValue;
    private T value;

    /**
     * Creates a new setting value with the given default value.
     *
     * @param defaultValue The default value of this setting.
     */
    public SettingValue(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
    }

    /**
     * Gets the default value of this setting.
     *
     * @return The default value
     */
    public T getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Sets the value of this setting.
     *
     * @param val The new value
     */
    public void setValue(T val) {
        this.value = val;
    }

    /**
     * Gets the current value of this setting.
     *
     * @return The current value
     */
    public T getValue() {
        return this.value;
    }
}
