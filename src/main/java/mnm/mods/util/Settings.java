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

    public Settings(File root, String name) {
        this.file = new File(root, name + ".json");
    }

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

    protected void saveSetting(String key, Boolean value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    protected void saveSetting(String key, Number value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    protected void saveSetting(String key, Character value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    protected void saveSetting(String key, String value) {
        if (saving) {
            jobj.addProperty(key, value);
        }
    }

    protected void saveSetting(String key, JsonElement jsonTree) {
        if (saving) {
            jobj.add(key, jsonTree);
        }
    }

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