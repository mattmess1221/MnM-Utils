package mnm.mods.util.config;

import java.util.HashMap;
import java.util.Map;

public class SettingMap<T> extends SettingSubtype<Map<String, T>, T> {

    public SettingMap(Class<T> type) {
        this(type, new HashMap<String, T>());
    }

    public SettingMap(Class<T> type, Map<String, T> map) {
        super(type, map);
    }

    public void put(String key, T value) {
        this.value.put(key, value);
    }

    public T get(String key) {
        return this.value.get(key);
    }

}
