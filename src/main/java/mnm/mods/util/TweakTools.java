package mnm.mods.util;

import net.minecraftforge.fml.common.FMLCommonHandler;

import com.mumfrey.liteloader.core.LiteLoader;

/**
 * Contains methods to detect different tweaks.
 *
 * @author Matthew
 */
public class TweakTools {

    /**
     * Returns whether LiteLoader is currently loaded.
     *
     * @return True if it's loaded
     */
    public static boolean isLiteLoaderLoaded() {
        boolean loaded = false;
        try {
            loaded = LiteLoader.getInstance() != null;
        } catch (Throwable t) {}
        return loaded;
    }

    /**
     * Returns whether FML is currently loaded.
     *
     * @return True if it's loaded
     */
    public static boolean isFMLLoaded() {
        boolean loaded = false;
        if (ForgeUtils.FML_INSTALLED) {
            loaded = FMLCommonHandler.instance() != null;
        }
        return loaded;
    }
}
