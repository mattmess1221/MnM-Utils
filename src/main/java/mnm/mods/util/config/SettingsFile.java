package mnm.mods.util.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mnm.mods.util.LogHelper;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Used for creating settings and saving/loading them in the JSON format. Start
 * by registering settings using {@link #registerSetting(String, SettingValue)}.
 * If your setting requires special handling for serialization, override
 * {@link #setupGson(GsonBuilder)} and use it to customize the {@link Gson}
 * object to your liking.
 *
 * @author Matthew Messinger
 */
public abstract class SettingsFile extends SettingObject<SettingsFile> {

    private static final LogHelper logger = LogHelper.getLogger();

    private final Gson gson;
    private final File file;

    private Map<String, SettingValue<?>> settings = Maps.newHashMap();

    /**
     * Creates settings with a settings file at root/name.json. Settings are not
     * automatically loaded.
     *
     * @param root The parent directory of the settings file
     * @param name The name of the settings file
     */
    public SettingsFile(File root, String name) {
        super(null);
        this.file = new File(root, name + ".json");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        setupGson(builder);
        this.gson = builder.create();
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

    /**
     * Called before the {@link Gson} object is created in order to allow
     * customization of it.
     *
     * @param builder The Gson Builder
     */
    protected void setupGson(GsonBuilder builder) {}

    /**
     * Saves the settings to file.
     */
    public final void saveSettingsFile() {
        JsonObject jobj = new JsonObject();
        saveSettings(jobj);
        try {
            file.getParentFile().mkdirs();
            FileUtils.write(file, gson.toJson(jobj));
        } catch (IOException e) {
            logger.error("Unable to save config", e);
        }
    }

    /**
     * Loads settings from file.
     */
    public final void loadSettingsFile() {
        file.getParentFile().mkdirs();
        JsonObject jobj = null;
        try {
            file.createNewFile();
            jobj = gson.fromJson(new FileReader(file), JsonObject.class);
        } catch (Exception e) {
            logger.warn("Loading of settings failed. Resetting to defaults.", e);
        }
        if (jobj == null) {
            jobj = new JsonObject();
        }

        loadSetting(jobj);
    }

    private void saveSettings(JsonObject output) {
        for (Entry<String, SettingValue<?>> value : settings.entrySet()) {
            output.add(value.getKey(), gson.toJsonTree(value.getValue().getValue()));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSetting(JsonObject input) {
        for (Entry<String, SettingValue<?>> value : settings.entrySet()) {
            try {
                if (input.has(value.getKey())) {
                    Class<?> type = value.getValue().getDefaultValue().getClass();
                    // prevent class cast error by reducing to interface
                    if (List.class.isAssignableFrom(type)) {
                        type = List.class;
                    } else if (Set.class.isAssignableFrom(type)) {
                        type = Set.class;
                    }
                    ((SettingValue<Object>) value.getValue()).setValue(gson.fromJson(input.get(value.getKey()), type));
                }
            } catch (Exception e) {
                logger.warn("Failed to load setting: " + value.getKey() + ". Using defaults.", e);
            }
        }
    }

    protected static <T> SettingValue<T> setting(T t) {
        return SettingValue.value(t);
    }
}
