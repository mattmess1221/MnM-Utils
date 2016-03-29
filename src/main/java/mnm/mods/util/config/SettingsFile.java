package mnm.mods.util.config;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Excluder;
import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.modconfig.AdvancedExposable;
import com.mumfrey.liteloader.util.PrivateFields;

import net.minecraft.util.EnumTypeAdapterFactory;

/**
 * Used for creating settings and saving/loading them in the JSON format. Start
 * by registering settings using {@link #registerSetting(String, SettingValue)}.
 * If your setting requires special handling for serialization, override
 * {@link #setupGson(GsonBuilder)} and use it to customize the {@link Gson}
 * object to your liking.
 *
 * @author Matthew Messinger
 */
public abstract class SettingsFile extends ValueObject implements AdvancedExposable {

    private transient final String path;
    private transient File file;

    public SettingsFile(String path, String name) {
        this.path = String.format("%s/%s.json", path, name);
    }

    @Override
    public void setupGsonSerialiser(GsonBuilder gsonBuilder) {
        new PrivateFields<GsonBuilder, Excluder>(GsonBuilder.class, new Obf("excluder") {}) {}
                .set(gsonBuilder, Excluder.DEFAULT); // grr
        gsonBuilder
                .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
                .registerTypeHierarchyAdapter(Value.class, new ValueSerializer());
    }

    @Override
    public File getConfigFile(File configFile, File configFileLocation, String defaultFileName) {
        if (file == null)
            file = new File(configFileLocation, path);
        return file;
    }

    public File getFile() {
        return file;
    }
}
