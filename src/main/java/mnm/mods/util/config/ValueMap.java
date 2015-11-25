package mnm.mods.util.config;

import java.util.Map;

import com.google.common.collect.Maps;

public class ValueMap<T> extends Value<Map<String, T>> {

    public ValueMap() {
        this.set(Maps.<String, T> newHashMap());
    }

    public void set(String key, T value) {
        this.get().put(key, value);
    }

    public T get(String key) {
        return this.get().get(key);
    }

    @Override
    public void set(Map<String, T> val) {
        super.set(Maps.newHashMap(val));
    }
}
