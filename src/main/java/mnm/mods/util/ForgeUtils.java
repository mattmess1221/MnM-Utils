package mnm.mods.util;

/**
 * Class for dealing with Forge.
 */
public final class ForgeUtils {

    /**
     * Detects if FML is on the classpath.<br>
     * Note: This only tells if FML is installed, not if it is actually loaded
     * into LaunchWrapper.
     */
    public static final boolean FML_INSTALLED = fmlInstalled();

    /**
     * Detects if Minecraft Forge is on the classpath.
     */
    public static final boolean FORGE_INSTALLED = forgeInstalled();

    private static boolean fmlInstalled() {
        boolean result = false;
        try {
            Class.forName("net.minecraftforge.fml.common.FMLCommonHandler");
            result = true;
        } catch (ClassNotFoundException e) {}
        return result;
    }

    private static boolean forgeInstalled() {
        boolean result = false;
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            result = true;
        } catch (ClassNotFoundException e) {}
        return result;
    }

    private ForgeUtils() {}

}
