package mnm.mods.util.config;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SettingObject<T> extends SettingValue<T> {

    protected Map<String, SettingValue<?>> settings = Maps.newHashMap();

    public SettingObject() {
        super(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        return (T) this;
    }

    @Override
    public void setValue(T val) {}

    public SettingValue<?> getValue(String key) {
        return settings.get(key);
    }

    public void setValue(String key, SettingValue<?> obj) {
        settings.put(key, obj);
    }

    /**
     * Registers a {@link SettingValue} to be loaded/saved. Call this during
     * construction. There is an issue with complex generic types. To resolve
     * this, create a non-generic class for it. Do something like this.
     *
     * <pre>
     * public SettingValue&lt;MyClassList&gt; myList = new SettingValue&lt;MyClassList&gt;(new MyClassList());
     *
     * public Settings() {
     *     registerSetting(&quot;myList&quot;, myList);
     * }
     *
     * public static class MyClassList extends ArrayList&lt;MyClass&gt; {
     *     private static final long serialVersionUID = 0L;
     * }
     * </pre>
     *
     * @param key The setting name
     * @param value The setting value
     */
    protected final void registerSetting(String key, SettingValue<?> value) {
        settings.put(key, value);
    }

    protected final void saveSettings(JsonObject output, Gson gson) {
        findSettings();
        for (Entry<String, SettingValue<?>> entry : settings.entrySet()) {
            String key = entry.getKey();
            SettingValue<?> value = entry.getValue();
            if (value instanceof SettingObject) {
                JsonObject obj = new JsonObject();
                ((SettingObject<?>) value).saveSettings(obj, gson);
                output.add(key, obj);
            } else {
                output.add(key, gson.toJsonTree(value.getValue()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected final void loadSetting(JsonObject input, Gson gson) {
        findSettings();
        for (Entry<String, SettingValue<?>> entry : settings.entrySet()) {
            try {
                if (input.has(entry.getKey())) {
                    SettingValue<Object> value = (SettingValue<Object>) entry.getValue();
                    JsonElement elem = input.get(entry.getKey());
                    if (value.getDefaultValue() != null) {
                        Class<?> type = value.getDefaultValue().getClass();
                        Type type1 = type;
                        if (value instanceof SettingSubtype<?, ?>) {
                            type1 = findActualType((SettingSubtype<?, T>) value, type);
                        }
                        value.setValue(gson.fromJson(elem, type1));
                    } else if (value instanceof SettingObject) {
                        ((SettingObject<?>) value).loadSetting(elem.getAsJsonObject(), gson);
                    }
                }
            } catch (Exception e) {
                LogManager.getLogger().warn("Failed to load setting: " + entry.getKey() + ". Using defaults.", e);
            }
        }
    }

    private static Type findActualType(SettingSubtype<?, ?> value, Class<?> itemType) {
        // prevent class cast error by reducing to interface
        Type type = null;
        if (List.class.isAssignableFrom(itemType)) {
            type = TypeUtils.parameterize(List.class, value.getType());
        } else if (Set.class.isAssignableFrom(itemType)) {
            type = TypeUtils.parameterize(Set.class, value.getType());
        } else if (Map.class.isAssignableFrom(itemType)) {
            type = TypeUtils.parameterize(Map.class, String.class, value.getType());
        }
        return type;
    }

    private void findSettings() {
        Class<?> current = getClass();
        Field[] fields = new Field[0];
        // get all the fields
        do {
            fields = ObjectArrays.concat(fields, current.getFields(), Field.class);
            current = current.getSuperclass();
        } while (current != null);

        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(Setting.class)) {
                    registerSetting(field.getName(), (SettingValue<?>) field.get(this));
                }
            } catch (Exception e) {
                LogManager.getLogger().warn(
                        "Unable to register setting " + getClass().getName() + "." + field.getName(), e);
            }
        }
    }

    public static <T> SettingValue<T> value(T t) {
        return new SettingValue<T>(t);
    }

    public static <T> SettingMap<T> map(Class<T> type) {
        return new SettingMap<T>(type);
    }

    public static <T> SettingList<T> list(Class<T> type, Iterable<T> coll) {
        return new SettingList<T>(type, coll);
    }

    public static <T> SettingList<T> list(Class<T> type, T... t) {
        return new SettingList<T>(type, t);
    }
}
