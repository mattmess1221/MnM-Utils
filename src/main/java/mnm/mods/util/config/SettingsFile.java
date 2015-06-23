package mnm.mods.util.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public abstract class SettingsFile<T extends SettingsFile<T>> extends SettingObject<T> {

    private static final Logger logger = LogManager.getLogger();

    private final Gson gson;
    private final File file;

    /**
     * Creates settings with a settings file at root/name.json. Settings are not
     * automatically loaded.
     *
     * @param root The parent directory of the settings file
     * @param name The name of the settings file
     */
    public SettingsFile(File root, String name) {
        this.file = new File(root, name + ".json");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        setupGson(builder);
        this.gson = builder.create();
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
        saveSettings(jobj, gson);
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

        loadSetting(jobj, gson);
    }
}
