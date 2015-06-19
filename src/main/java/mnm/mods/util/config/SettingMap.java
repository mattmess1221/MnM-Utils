package mnm.mods.util.config;

import java.util.HashMap;
import java.util.Map;

public class SettingMap<T> extends SettingValue<Map<String, T>> {

    public SettingMap() {
        this(new HashMap<String, T>());
    }

    public SettingMap(Map<String, T> map) {
        super(map);
    }

    public void put(String key, T value) {
        this.value.put(key, value);
    }

    public T get(String key) {
        return this.value.get(key);
    }

}
