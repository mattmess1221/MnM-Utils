package mnm.mods.util;

public class SettingValue<T> {

    private final T defaultValue;
    private T value;

    public SettingValue(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public void setValue(T val) {
        this.value = val;
    }

    public T getValue() {
        return this.value;
    }
}
