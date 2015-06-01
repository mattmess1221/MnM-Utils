package mnm.mods.util;

/**
 * A wrapper that saves a default value.
 *
 * @param <T> The type to wrap
 */
public class SettingValue<T> {

    private final T defaultValue;
    protected T value;

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

    public static <T> SettingValue<T> value(T t) {
        return new SettingValue<T>(t);
    }

    public static <T extends ISettingObject> SettingObject<T> object(T object) {
        return new SettingObject<T>(object);
    }

    public static <T> SettingList<T> list(Iterable<T> coll) {
        return new SettingList<T>(coll);
    }

    public static <T> SettingList<T> list(T... t) {
        return new SettingList<T>(t);
    }
}
