package mnm.mods.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Settings {

    private static final LogHelper logger = LogHelper.getLogger();
    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private boolean saving;
    private JsonObject jobj;
    protected final File file;

    /**
     * Creates settings with a settings file at root/name.json. Settings are not
     * automatically loaded.
     *
     * @param root The parent directory of the settings file
     * @param name The name of the settings file
     */
    public Settings(File root, String name) {
        this.file = new File(root, name + ".json");
    }

    /**
     * Saves the settings to file.
     */
    public final void saveSettingsFile() {
        jobj = new JsonObject();
        saving = true;
        saveSettings();
        saving = false;
        try {
            file.getParentFile().mkdirs();
            FileUtils.write(file, gson.toJson(jobj));
        } catch (IOException e) {
            logger.error("Unable to save config", e);
        }
    }

    /**
     * Saves a boolean value.
     *
     * @param key
     * @param value
     */
    protected void saveSetting(String key, Boolean value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    /**
     * Saves a number value.
     *
     * @param key
     * @param value
     */
    protected void saveSetting(String key, Number value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    /**
     * Saves a character value.
     *
     * @param key
     * @param value
     */
    protected void saveSetting(String key, Character value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    /**
     * Saves a string value.
     *
     * @param key
     * @param value
     */
    protected void saveSetting(String key, String value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    /**
     * Saves an advanced json element.
     *
     * @param key
     * @param jsonTree
     */
    protected void saveSetting(String key, JsonElement jsonTree) {
        if (saving) {
            jobj.add(key, jsonTree);
        }
    }

    /**
     * Loads settings from file.
     */
    public final void loadSettingsFile() {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            jobj = gson.fromJson(new FileReader(file), JsonObject.class);
        } catch (Exception e) {
            logger.warn("Loading of settings failed. Resetting to defaults.", e);
        }
        if (jobj == null) {
            jobj = new JsonObject();
        }

        for (Entry<String, JsonElement> ele : jobj.entrySet()) {
            loadSetting(ele.getKey(), ele.getValue());
        }
    }

    /**
     * Called when the settings will be saved. Call saveSetting.
     *
     * @param jobj Save to this
     */
    protected abstract void saveSettings();

    /**
     * Loads the settings one by one.
     *
     * @param setting
     * @param value
     */
    protected abstract void loadSetting(String setting, JsonElement value);

}