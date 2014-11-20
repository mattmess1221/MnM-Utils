package mnm.mods.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

public class TweakTools {

    /**
     * Returns whether LiteLoader is loaded into LaunchWrapper.
     *
     * @return True if it's loaded
     */
    public static boolean isLiteLoaderLoaded() {
        return isTweakLoaded("com.mumfrey.liteloader.launch.LiteLoaderTweaker");
    }

    /**
     * Returns whether FML is loaded into LaunchWrapper.
     *
     * @return True if it's loaded
     */
    public static boolean isFMLLoaded() {
        return isTweakLoaded("net.minecraftforge.fml.common.launcher.FMLTweaker");
    }

    /**
     * Gets if a given tweak is loaded into LaunchWrapper.
     *
     * @param name The fully qualified class name
     * @return True if it's loaded
     */
    public static boolean isTweakLoaded(String name) {
        boolean load = false;
        @SuppressWarnings("unchecked")
        List<ITweaker> tweakers = (List<ITweaker>) Launch.blackboard.get("Tweaks");

        // Iterate through the tweaks and check the class name
        Iterator<ITweaker> iter = tweakers.iterator();
        while (!load && iter.hasNext()) {
            ITweaker tweak = iter.next();
            String className = tweak.getClass().getName();
            load = className.equals(name);
        }
        return load;
    }
}
