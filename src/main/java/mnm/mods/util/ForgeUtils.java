package mnm.mods.util;

public final class ForgeUtils {

    public static final boolean FML_INSTALLED = fmlInstalled();
    public static final boolean FORGE_INSTALLED = forgeInstalled();

    private static boolean fmlInstalled() {
        boolean result = false;
        try {
            Class.forName("net.minecraftforge.fml.common.FMLCommonHandler");
            result = true;
        } catch (ClassNotFoundException e) {
        }
        return result;
    }

    private static boolean forgeInstalled() {
        boolean result = false;
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            result = true;
        } catch (ClassNotFoundException e) {
        }
        return result;
    }

    private ForgeUtils() {
    }

}
