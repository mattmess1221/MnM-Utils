package mnm.mods.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

public class TweakTools {

    public static boolean isLiteLoaderLoaded() {
        return isTweakLoaded("com.mumfrey.liteloader.launch.LiteLoaderTweaker");
    }

    public static boolean isFMLLoaded() {
        return isTweakLoaded("net.minecraftforge.fml.common.launcher.FMLTweaker");
    }

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
